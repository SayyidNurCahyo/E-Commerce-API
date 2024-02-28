package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, String> {
}
