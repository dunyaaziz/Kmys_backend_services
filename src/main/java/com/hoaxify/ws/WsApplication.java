package com.hoaxify.ws;

import com.hoaxify.ws.model.Role;
import com.hoaxify.ws.repository.RoleRepository;
import com.hoaxify.ws.model.User;
import com.hoaxify.ws.service.UserService;
import com.hoaxify.ws.model.vm.ERole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class WsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsApplication.class, args);
    }
//
//    @Bean
//    @Profile("dev")
//    CommandLineRunner createInitialUsers(UserService userService, RoleRepository roleRepository) {
//        return (args) -> {
//            try {
//                userService.getByUsername("user1");
//                roleRepository.findByName(ERole.ROLE_USER);
//            }catch (Exception e){
//                Role roleAdmin = new Role(ERole.ROLE_ADMIN);
//                Role role = new Role(ERole.ROLE_USER);
//                Set<Role> roles = new HashSet<>();
//                roles.add(roleAdmin);
//                roles.add(role);
//                roleRepository.saveAll(roles);
//
//                for(int i=0;i<10;i++){
//                    User user = new User();
//                    user.setDisplayName("display"+i);
//                    user.setEmail("selam@gmail.com");
//                    user.setPassword("Ss.123456");
//
//                    if(i==1){
//                        user.setIsPaid(true);
//                        user.setLisanceKey(userService.getSerialKey());
//                    }else{
//                        user.setIsPaid(false);
//                    }
//                    if (i == 0) {
//                        Set<Role> rolesADMN = new HashSet<>();
//                        rolesADMN.add(roleRepository.findByName(ERole.ROLE_ADMIN));
//                        user.setUsername("admin");
//                        user.setRoles(rolesADMN);
//
//
//                    }else {
//                        Set<Role> rolesUser = new HashSet<>();
//                        rolesUser.add(roleRepository.findByName(ERole.ROLE_USER));
//                        user.setFirstLoginDate(new Date());
//                        user.setUsername("user"+i);
//                        user.setRoles(rolesUser);
//                    }
//                    userService.save(user);
//                }
//            }
//
//
//        };
  //  }

}
