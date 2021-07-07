package com.hoaxify.ws.controller;

import com.hoaxify.ws.configuration.UserDetailImpl;
import com.hoaxify.ws.model.User;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.model.vm.UserPasswordChangeVM;
import com.hoaxify.ws.model.vm.UserUpdateVM;
import com.hoaxify.ws.model.vm.UserVM;
import com.hoaxify.ws.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;

@RestController
@RequestMapping("/api/1.0")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public GenericResponse createUser(@Valid @RequestBody User user) {
        userService.save(user);
        return new GenericResponse("user created");
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<UserVM> getUsers(Pageable page, @CurrentUser UserDetailImpl user) {

        return userService.getUsers(page, user).map(UserVM::new);
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("#username == principal.username or hasRole('ROLE_ADMIN')")
    public UserVM getUser(@PathVariable String username){
        User user = userService.getByUsername(username);
        return new UserVM(user);
    }
// or hasRole('ROLE_USER')
    @PutMapping("/users/{username}" )
    @PreAuthorize("#username == principal.username")
    public UserVM updateUser(@Valid @RequestBody UserUpdateVM updateVM, @PathVariable String username){
        User user = userService.updateUser(username,updateVM);
        return new UserVM(user);
    }
    @PutMapping("/paid/{username}" )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean updateUserIsPaid(@Valid @PathVariable String username){
        return userService.updateUserIsPaid(username);
    }

    @PutMapping("/changePass/{username}")
    @PreAuthorize("#username == principal.username")
    public GenericResponse  changeUserPassword(@Valid @RequestBody UserPasswordChangeVM userPasswordChangeVM, @PathVariable String username){
        userService.changePassword(username,userPasswordChangeVM);
        return new GenericResponse("Password is changed");
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    //@GetMapping("/downloadFile")
    //@ResponseBody
    //@RequestMapping(value = "/downloadFile", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/downloadFile")
    @ResponseBody
    public void downloadPDFResource(HttpServletResponse response, @CurrentUser UserDetailImpl user) throws FileNotFoundException {
          userService.downloadFile(response,user);


    }
}
