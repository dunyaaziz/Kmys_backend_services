package com.hoaxify.ws.controller;

import com.hoaxify.ws.model.vm.DesktopUserVM;
import com.hoaxify.ws.model.vm.UserUpdateVM;
import com.hoaxify.ws.service.DesktopUserService;
import com.hoaxify.ws.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/desktopApi/1.0")
public class DesktopAuthController {
    @Autowired
    DesktopUserService desktopUserService;

    @PostMapping("/users")
    public Boolean userLogin(@RequestBody DesktopUserVM desktopUserVM){
        return desktopUserService.userLogin(desktopUserVM);
    }

    @PostMapping("/checkLogin")
    public String checkLogin(@RequestBody DesktopUserVM desktopUserVM){
        return desktopUserService.userLoginBefore(desktopUserVM.getMacAddress());
    }

}
