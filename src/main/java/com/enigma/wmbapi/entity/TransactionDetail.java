package com.enigma.wmbapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_bill_detail")
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "bill_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    @Column(name = "qty")
    private Float qty;

    @Column(name = "price")
    private Float price;
}
