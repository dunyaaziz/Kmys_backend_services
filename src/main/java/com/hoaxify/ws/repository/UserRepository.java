package com.hoaxify.ws.repository;

import com.hoaxify.ws.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    List<User> findByMacAddress(String macAddress);
    User findByUsernameAndPasswordAndMacAddressAndLisanceKey(String username,String password, String macAddress, String lisanceKey);
    Boolean existsByUsername(String username);
    Boolean existsByMacAddress(String macAddress);
    Boolean existsByLisanceKey(String lisanceKey);
    Page<User> findByUsernameNot(String username, Pageable page);
}
