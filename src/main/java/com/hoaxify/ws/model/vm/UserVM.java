package com.hoaxify.ws.model.vm;

import com.hoaxify.ws.configuration.UserDetailImpl;
import com.hoaxify.ws.model.User;
import lombok.Data;

@Data
public class UserVM {
    private String username;

    private String displayName;

    private String image;

    private ERole Role;

    private Boolean isPaid;

    private String lisanceKey;

    public UserVM(User user) {
        this.setUsername(user.getUsername());
        this.setDisplayName(user.getDisplayName());
        this.setImage(user.getImage());
        this.setIsPaid(user.getIsPaid());
        this.setLisanceKey(user.getLisanceKey());
        if(!user.getRoles().isEmpty()){
            this.setRole(user.getRoles().iterator().next().getName());
        }else {
            this.setRole(ERole.ROLE_USER);
        }
    }

    public UserVM(UserDetailImpl user) {
        this.setUsername(user.getUsername());
        this.setDisplayName(user.getDisplayName());
        this.setImage(user.getImage());
        this.setIsPaid(user.getIsPaid());
        this.setLisanceKey(user.getLisanceKey());
        if(user.getRole()!=null){
            this.setRole(user.getRole());
        }else {
            this.setRole(ERole.ROLE_USER);
        }
    }
}
