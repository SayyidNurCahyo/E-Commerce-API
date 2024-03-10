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
    @Query(value = "SELECT * FROM t_bill WHERE customer_id = :customer_id", nativeQuery = true)
    Page<Transaction> findCustomerId(@Param(value = "customer_id") String customer_id, Pageable pageable);
    @Query(value = "SELECT * FROM t_bill WHERE customer_id = :customer_id", nativeQuery = true)
    List<Transaction> findTr(@Param(value = "customer_id") String customer_id);
}
