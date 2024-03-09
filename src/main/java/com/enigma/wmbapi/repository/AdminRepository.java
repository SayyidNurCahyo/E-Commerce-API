package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Admin;
import com.enigma.wmbapi.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    @Query(value = "SELECT * FROM m_admin WHERE name = :name OR phone_number = :phone", nativeQuery = true)
    Page<Admin> findAdmin(@Param("name") String name, @Param("phone") String phone, Pageable pageable);
}
