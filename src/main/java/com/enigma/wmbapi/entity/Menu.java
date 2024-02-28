package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = ConstantTable.MENU)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;
}


