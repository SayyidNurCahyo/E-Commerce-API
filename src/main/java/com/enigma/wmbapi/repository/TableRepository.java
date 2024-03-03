package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Menu;
import com.enigma.wmbapi.entity.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, String> {
    @Query(value = "SELECT * FROM m_table WHERE name = :name",nativeQuery = true)
    Page<Table> findTable(@Param("name") String name, Pageable pageable);
}
