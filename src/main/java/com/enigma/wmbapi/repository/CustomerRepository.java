package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query(value = "SELECT * FROM m_customer WHERE name = :name OR phone_number = :phone", nativeQuery = true)
    Page<Customer> findCustomer(@Param("name") String name, @Param("phone") String phone, Pageable pageable);
}
