package com.narola.controller;

import com.narola.model.Person;
import com.narola.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@Controller
public class AuthController extends BaseController {

    @Autowired
    IAuthService authService;
    public String codeVerifier;

    @GetMapping("/")
    public String hello() {
        return "login.html";
    }

    @GetMapping("/get_token")
    public ResponseEntity<Person> getToken(@RequestParam String code) throws URISyntaxException, IOException {
        String accessToken = authService.getToken(code);
        Person person = authService.getPersonDetails(accessToken);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping("/get_pkce_token")
    public ResponseEntity<Person> getPKCEToken(@RequestParam String code) throws URISyntaxException, IOException {
        String accessToken = authService.getPKCEToken(code, codeVerifier);
        Person person = authService.getPersonDetails(accessToken);
        return ResponseEntity.ok().body(person);
    }

    @PostMapping(value = "/get_code")
    public RedirectView generateAuthCodeRequest() throws URISyntaxException {
        String uri = authService.generateAuthCodeRequest();
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(uri);
        return redirectView;
    }

    @PostMapping(value = "/get_pkce_code")
    public RedirectView generatePKCEAuthCodeRequest() throws URISyntaxException, NoSuchAlgorithmException, UnsupportedEncodingException {
        codeVerifier = authService.generateRandomString();
        String uri = authService.generatePKCEAuthCodeRequest(codeVerifier);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(uri);
        return redirectView;
    }

}
