package com.hoaxify.ws.model;

import com.hoaxify.ws.model.vm.UserVM;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;

    private UserVM user;
}
