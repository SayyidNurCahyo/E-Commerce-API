package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANS_TYPE)
public class TransType {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;
//    @ManyToMany(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private TransTypeId id;

    @Column(name = "description")
    private String description;
}


