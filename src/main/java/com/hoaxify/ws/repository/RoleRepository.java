package com.hoaxify.ws.repository;

import com.hoaxify.ws.model.Role;
import com.hoaxify.ws.model.vm.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}
