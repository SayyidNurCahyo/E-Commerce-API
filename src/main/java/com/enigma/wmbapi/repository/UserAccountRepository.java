package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    @Query(value = "SELECT * FROM m_user_account WHERE username = :username", nativeQuery = true)
    Optional<UserAccount> findUserAccount(@Param("username") String username);
}
