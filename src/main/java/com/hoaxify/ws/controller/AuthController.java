package com.hoaxify.ws.controller;

import com.hoaxify.ws.configuration.UserDetailImpl;
import com.hoaxify.ws.model.AuthResponse;
import com.hoaxify.ws.model.Credentials;
import com.hoaxify.ws.service.AuthService;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.service.UserService;
import com.hoaxify.ws.model.vm.UserVM;
import com.hoaxify.ws.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.GeneratedValue;

@RestController
public class AuthController {


    @Autowired
    AuthService authService;

    @PostMapping("/api/1.0/auth")
    AuthResponse handleAuthentication(@RequestBody Credentials credentials) {
        return  authService.authenticate(credentials);
    }
    @PostMapping("/api/1.0/logout")
    GenericResponse handleLogout(@RequestHeader(name = "Authorization") String authorization) {
        String token = authorization.substring(7);
        authService.clearToken(token);
        return new GenericResponse("Logout success");
    }

}
