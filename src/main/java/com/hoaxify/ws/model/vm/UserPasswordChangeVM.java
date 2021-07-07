package com.hoaxify.ws.model.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserPasswordChangeVM {

    private String oldPassword;

    @NotNull
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).*$", message = "{hoaxify.constraints.password.Pattern.message}")
    private String newPassword;

}
