package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Table;
import com.enigma.wmbapi.entity.TransType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransTypeRepository extends JpaRepository<TransType, String> {
}
