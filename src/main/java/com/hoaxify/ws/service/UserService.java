package com.hoaxify.ws.service;

import com.hoaxify.ws.configuration.UserDetailImpl;
import com.hoaxify.ws.error.InvalidOldPasswordException;
import com.hoaxify.ws.error.NotFoundException;
import com.hoaxify.ws.file.FileService;
import com.hoaxify.ws.model.Role;
import com.hoaxify.ws.model.User;
import com.hoaxify.ws.model.vm.ERole;
import com.hoaxify.ws.model.vm.UserPasswordChangeVM;
import com.hoaxify.ws.model.vm.UserUpdateVM;
import com.hoaxify.ws.repository.RoleRepository;
import com.hoaxify.ws.repository.UserRepository;
import com.hoaxify.ws.shared.SerialKeyGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    FileService fileService;

    RoleRepository roleRepository;

    SerialKeyGenerator serialKeyGenerator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FileService fileService, RoleRepository roleRepository,SerialKeyGenerator serialKeyGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
        this.roleRepository = roleRepository;
        this.serialKeyGenerator = serialKeyGenerator;
    }

    @Transactional
    public void save(User user) {
        if(user.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_USER));
            user.setRoles(roles);
        }
        user.setFirstLoginDate(new Date());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setIsPaid(false);
        userRepository.save(user);
    }

    public void updateLastLogin(UserDetailImpl user){
        User loggedInUSer = userRepository.findByUsername(user.getUsername());
        loggedInUSer.setLastLoginDate(new Date());
        userRepository.save(loggedInUSer);
    }

    public Page<User> getUsers(Pageable page, UserDetailImpl user) {
        if(user != null){
            return userRepository.findByUsernameNot(user.getUsername(), page);
        }
        return userRepository.findAll(page);
    }

    public User getByUsername(String username) {
        User inDB = userRepository.findByUsername(username);
        if(inDB == null){
            throw new NotFoundException();
        }
        return inDB;
    }

    public User updateUser(String username, UserUpdateVM updatedUser) {
        User inDB = getByUsername(username);
        inDB.setDisplayName(updatedUser.getDisplayName());
        if(updatedUser.getImage() != null){
            String oldImageName = inDB.getImage();
//            inDB.setImage(updatedUser.getImage());
            try {
                String storedFileName = fileService.writeBase64EncodedStringToFile(updatedUser.getImage());
                inDB.setImage(storedFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileService.deleteFile(oldImageName);
        }
        return userRepository.save(inDB);

        }
    public Boolean updateUserIsPaid(String username) {
        User inDB = getByUsername(username);
        if(inDB.getIsPaid()==null || inDB.getIsPaid()==false) {
            inDB.setTokenGeneratedDate(new Date());
            inDB.setIsPaid(true);
            inDB.setLisanceKey(getSerialKey());

        }
        else{
            inDB.setIsPaid(false);
            inDB.setLisanceKey(null);
           }

        //inDB.setIsPaid(!inDB.getIsPaid());
        userRepository.save(inDB);
        return inDB.getIsPaid();
    }

    public void changePassword(String username, UserPasswordChangeVM userPasswordChangeVM) {
        User inDB = getByUsername(username);

        if(!this.passwordEncoder.matches(userPasswordChangeVM.getOldPassword(),inDB.getPassword())){
            throw new InvalidOldPasswordException("The password you entered does not match our records.");
        }

        inDB.setPassword(this.passwordEncoder.encode(userPasswordChangeVM.getNewPassword()));
        userRepository.save(inDB);

    }

    public void downloadFile(HttpServletResponse response, UserDetailImpl user) throws FileNotFoundException {
       User inDB = getByUsername(user.getUsername());
       if(inDB.getIsPaid()){
           fileService.downloadFile(response);

        }
           else {
           throw new NotFoundException("you should paid firstly");
        }

           }

    public String getSerialKey(){
        boolean isSame = true;
        String serialKey = null;
        while(isSame){
            serialKey = serialKeyGenerator.beautifyDigits();
            if(!userRepository.existsByLisanceKey(serialKey)){
                isSame = false;
            }
        }
        return serialKey;
    }


}
