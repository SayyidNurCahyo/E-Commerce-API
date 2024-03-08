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
    @Column(name = "token")
    private String token;
    @Column(name = "redirect_url")
    private String redirectURL;
    @Column(name = "transaction_status")
    private String transactionStatus;
    @OneToOne(mappedBy = "payment")
    private Transaction transaction;
}
