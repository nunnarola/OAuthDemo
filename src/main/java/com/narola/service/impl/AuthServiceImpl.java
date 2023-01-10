package com.narola.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narola.exception.OAuthException;
import com.narola.model.Person;
import com.narola.service.IAuthService;
import com.narola.util.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthServiceImpl implements IAuthService {
    @Override
    public String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 45;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public Person getPersonDetails(String accessToken) throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder("https://people.googleapis.com/v1/people/me");
            uriBuilder.addParameter("requestMask.includeField", "person.names,person.photos,person.email_addresses");
            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("Authorization", "Bearer " + accessToken);

            HttpResponse httpResponse = httpclient.execute(httpGet);
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            Person person = objectMapper.readValue(responseBody, Person.class);
            httpclient.close();
            return person;
        }
    }

    @Override
    public String getToken(String code) throws URISyntaxException, IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put("grant_type", "authorization_code");
            attributesMap.put("code", code);
            attributesMap.put("redirect_uri", Constants.REDIRECT_URI);
            attributesMap.put("client_id", Constants.CLIENT_ID);
            attributesMap.put("client_secret", Constants.CLIENT_SECRET);

            URIBuilder uriBuilder = new URIBuilder("https://oauth2.googleapis.com/token");
            attributesMap.entrySet()
                    .forEach(stringEntry -> uriBuilder.addParameter(stringEntry.getKey(), stringEntry.getValue()));
            URI uri = uriBuilder.build();
            HttpPost httppost = new HttpPost(uri);
            HttpResponse httpResponse = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new OAuthException("Exception while fetching token");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);
            String accessToken = responseMap.get("access_token");
            return accessToken;
        }
    }

    @Override
    public String getPKCEToken(String code, String codeVerifier) throws URISyntaxException, IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put("grant_type", "authorization_code");
            attributesMap.put("code", code);
            attributesMap.put("redirect_uri", "http://localhost:8080/get_pkce_token");
            attributesMap.put("client_id", Constants.CLIENT_ID);
            attributesMap.put("client_secret", Constants.CLIENT_SECRET);
            attributesMap.put("code_verifier", codeVerifier);

            URIBuilder uriBuilder = new URIBuilder("https://oauth2.googleapis.com/token");
            attributesMap.entrySet()
                    .forEach(stringEntry -> uriBuilder.addParameter(stringEntry.getKey(), stringEntry.getValue()));
            URI uri = uriBuilder.build();
            HttpPost httppost = new HttpPost(uri);
            HttpResponse httpResponse = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new OAuthException("Exception while fetching token");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);
            return responseMap.get("access_token");
        }
    }

    @Override
    public String generateAuthCodeRequest() throws URISyntaxException {
        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put("response_type", "code");
        attributesMap.put("client_id", Constants.CLIENT_ID);
        attributesMap.put("redirect_uri", Constants.REDIRECT_URI);
        attributesMap.put("scope", "openid profile email");

        URIBuilder uriBuilder = new URIBuilder("https://accounts.google.com/o/oauth2/v2/auth");
        attributesMap.entrySet()
                .forEach(stringEntry -> uriBuilder.addParameter(stringEntry.getKey(), stringEntry.getValue()));
        return uriBuilder.build().toString();
    }

    private String generateCodeChallenge(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    @Override
    public String generatePKCEAuthCodeRequest(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException {

        String codeChallenge = this.generateCodeChallenge(codeVerifier);

        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put("response_type", "code");
        attributesMap.put("client_id", Constants.CLIENT_ID);
        attributesMap.put("redirect_uri", "http://localhost:8080/get_pkce_token");
        attributesMap.put("scope", "openid profile email");
        attributesMap.put("code_challenge", codeChallenge);
        attributesMap.put("code_challenge_method", "S256");
        attributesMap.put("access_type", "offline");
        attributesMap.put("prompt", "consent");

        URIBuilder uriBuilder = new URIBuilder("https://accounts.google.com/o/oauth2/v2/auth");
        attributesMap.entrySet()
                .forEach(stringEntry -> uriBuilder.addParameter(stringEntry.getKey(), stringEntry.getValue()));
        URI uri = uriBuilder.build();
        return uri.toString();
    }
}
