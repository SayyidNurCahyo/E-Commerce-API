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

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query(value = "select c.id, c.name, c.phone_number, c.user_account_id from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where (c.name like :name or c.phone_number like :phone) and ua.is_enable = true", nativeQuery = true)
    Page<Customer> findCustomer(@Param("name") String name, @Param("phone") String phone, Pageable pageable);

    @Query(value = "select c.id, c.name, c.phone_number, c.user_account_id from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where ua.is_enable = true", nativeQuery = true)
    Page<Customer> findAllCustomer(Pageable pageable);

    @Query(value = "select c.id, c.name, c.phone_number, c.user_account_id from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where c.id = :id and ua.is_enable = true", nativeQuery = true)
    Optional<Customer> findByIdCustomer(@Param("id") String id);
}
