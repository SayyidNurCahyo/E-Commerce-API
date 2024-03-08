package com.enigma.wmbapi.entity;

import com.enigma.wmbapi.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANSACTION)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "trans_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private com.enigma.wmbapi.entity.Table table;

    @ManyToOne
    @JoinColumn(name = "trans_type", referencedColumnName = "id", nullable = false)
    private TransType transType;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<TransactionDetail> transactionDetails;

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;
}
