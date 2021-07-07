package com.hoaxify.ws.service;

import com.hoaxify.ws.file.FileService;
import com.hoaxify.ws.model.User;
import com.hoaxify.ws.model.vm.DesktopUserVM;
import com.hoaxify.ws.repository.RoleRepository;
import com.hoaxify.ws.repository.UserRepository;
import com.hoaxify.ws.shared.SerialKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DesktopUserService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    SerialKeyGenerator serialKeyGenerator;

    public DesktopUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,SerialKeyGenerator serialKeyGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.serialKeyGenerator = serialKeyGenerator;


    }
    public String userLoginBefore(String macAddress) {
        if (userRepository.existsByMacAddress(macAddress)) {
            List<User> userInDb = userRepository.findByMacAddress(macAddress);
            for (User user:userInDb ) {
                if(checkLisanceExpire(user.getTokenGeneratedDate())) {
                    return user.getUsername();
                }
               }
            }
        return null;
    }

    private Boolean checkLisanceExpire(Date tokenGeneratedDate) {
        long diffInMillies = Math.abs(new Date().getTime() - tokenGeneratedDate.getTime());
        long noOfDaysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (noOfDaysBetween > 365) {
            return false;
        }
        return true;
    }


    public Boolean userLogin(DesktopUserVM desktopUserVM) {
        //String serialKey = getSerialKey();

        if(userRepository.existsByUsername(desktopUserVM.getUsername())){
            User userInDb = userRepository.findByUsername(desktopUserVM.getUsername());
            if(this.passwordEncoder.matches(desktopUserVM.getPassword(),userInDb.getPassword())){
                if(!((userInDb.getMacAddress() == null) || (userInDb.getMacAddress() == "") || userInDb.getMacAddress().equals("null"))){
                    if(!userInDb.getMacAddress().equals(desktopUserVM.getMacAddress())){
                        return false;
                    }
                }
                else{
                    userInDb.setMacAddress(desktopUserVM.getMacAddress());

                }
                if (userInDb.getLisanceKey() != null && checkLisanceExpire(userInDb.getTokenGeneratedDate())) {
                if(!((userInDb.getLisanceKey() == null) || (userInDb.getLisanceKey() == "") || userInDb.getLisanceKey().equals("null"))) {
                    if (!userInDb.getLisanceKey().equals(desktopUserVM.getLisanceKey())) {
                            return false;
                        }
                    }
                }
                userRepository.save(userInDb);
                return true;
            }
        }
        return false;
    }



}
