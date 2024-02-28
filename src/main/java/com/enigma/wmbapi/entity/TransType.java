package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = ConstantTable.TRANS_TYPE)
public class TransType {
    @Id
    private String id;

    @Column(name = "description")
    private String description;
}


