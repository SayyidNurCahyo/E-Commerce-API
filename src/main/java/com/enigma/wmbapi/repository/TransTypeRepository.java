package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.constant.TransTypeId;
import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.entity.TransType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransTypeRepository extends JpaRepository<TransType, String> {
    Optional<TransType> findById(TransTypeId id);
}
