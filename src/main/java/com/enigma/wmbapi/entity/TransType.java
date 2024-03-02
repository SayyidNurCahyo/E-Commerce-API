package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANS_TYPE)
public class TransType {
    @Id
    private TransTypeId id;

    @Column(name = "description")
    private String description;
}


