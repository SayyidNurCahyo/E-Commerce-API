package com.enigma.wmbapi.repository;

import com.enigma.wmbapi.entity.Customer;
import com.enigma.wmbapi.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
    @Query(value = "SELECT * FROM m_menu where name like :name OR price = :price",nativeQuery = true)
    Page<Menu> findMenu(@Param("name") String name, @Param("price") Long price, Pageable pageable);
}
