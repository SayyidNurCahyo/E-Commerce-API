package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
