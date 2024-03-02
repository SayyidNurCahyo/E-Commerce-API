package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@jakarta.persistence.Table(name = ConstantTable.TABLE)
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;
}


