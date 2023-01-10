package com.narola.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuickStart {
    private static final String APPLICATION_NAME = "Google People API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = new ArrayList<>(PeopleServiceScopes.all());
    private static final String CREDENTIALS_FILE_PATH = "/service-credentials.json";


    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.

        InputStream in = PeopleQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(SCOPES);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        PeopleService service =
                new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        // Request 10 connections.
        Person profile = service.people().get("people/me")
                .setPersonFields("names,emailAddresses")
                .execute();


        List<Name> names = profile.getNames();
        List<EmailAddress> emailAddresses = profile.getEmailAddresses();
        if (names != null && names.size() > 0) {
            System.out.println("Name: " + profile.getNames().get(0)
                    .getDisplayName());
        }
        else if (emailAddresses != null && emailAddresses.size() > 0) {
            System.out.println("Email Address: " + profile.getEmailAddresses().get(0)
                    .getDisplayName());
        }
        else {
            System.out.println("No names available for connection.");
        }

    }
}
