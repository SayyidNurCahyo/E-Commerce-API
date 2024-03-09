package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.PAYMENT)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "redirect_url", nullable = false)
    private String redirectURL;
    @Column(name = "transaction_status", nullable = false)
    private String transactionStatus;
    @OneToOne(mappedBy = "payment")
    private Transaction transaction;
}
