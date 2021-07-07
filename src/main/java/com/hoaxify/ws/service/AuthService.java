package com.hoaxify.ws.service;

import com.hoaxify.ws.configuration.UserDetailImpl;
import com.hoaxify.ws.error.AuthException;
import com.hoaxify.ws.model.AuthResponse;
import com.hoaxify.ws.model.Credentials;
import com.hoaxify.ws.model.Token;
import com.hoaxify.ws.model.User;
import com.hoaxify.ws.model.vm.UserVM;
import com.hoaxify.ws.repository.TokenRepository;
import com.hoaxify.ws.repository.UserRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
public class AuthService {

    UserService userService;

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    TokenRepository tokenRepository;

    public AuthService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponse authenticate(Credentials credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        UserDetailImpl inDB = UserDetailImpl.build(user);

        if (inDB == null) {
            throw new AuthException();
        }
        boolean matches = passwordEncoder.matches(credentials.getPassword(), inDB.getPassword());
        if (!matches) {
            throw new AuthException();
        }
        UserVM userVM = new UserVM(inDB);
        Date expiresAt = new Date(System.currentTimeMillis() + 10 * 1000);
        String token = generateRandomToken();

        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUser(user);
        tokenEntity.setExpiredAt(System.currentTimeMillis());
        tokenRepository.save(tokenEntity);

        AuthResponse response = new AuthResponse();
        response.setUser(userVM);
        response.setToken(token);
        userService.updateLastLogin(inDB);
        return response;
    }

    @Transactional
    public UserDetails getUserDetails(String token) {
        Token optionalToken = tokenRepository.findByToken(token);
        if (optionalToken == null) {
            return null;
        }
        return UserDetailImpl.build(optionalToken.getUser());

    }

    public String generateRandomToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Scheduled(fixedRate = 4 * 60 * 60 * 1000)
    public void deleteExpiredToken(){

        List<Token> tokenToBeDeleted = tokenRepository.findAll();
        for(Token token: tokenToBeDeleted){
            if(token.getExpiredAt()==null){
                tokenRepository.deleteById(token.getId());
            }else if((System.currentTimeMillis() - (2 * 60 * 60 * 1000)) > token.getExpiredAt()){
                tokenRepository.deleteById(token.getId());
            }
        }
    }
    @Transactional
    public void clearToken(String token) {
        tokenRepository.deleteByToken(token);
    }
}
