package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query(value = "SELECT * FROM t_transaction WHERE customer_id = :customerId", nativeQuery = true)
    Optional<Page<Transaction>> findAllByCustomerId(@Param("customerId") String customerId, Pageable pageable);
}
