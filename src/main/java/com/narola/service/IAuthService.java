package com.narola.service;

import com.narola.model.Person;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public interface IAuthService {
    String generateRandomString();

    Person getPersonDetails(String accessToken) throws IOException, URISyntaxException;

    String getToken(String code) throws URISyntaxException, IOException;

    String getPKCEToken(String code, String codeVerifier) throws URISyntaxException, IOException;

    String generateAuthCodeRequest() throws URISyntaxException;

    String generatePKCEAuthCodeRequest(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException;
}
