package com.bank.entity;

import com.bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class BankTransaction extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer transactionId; 			//Integer (PK)

        @Column(unique = true)
        private String transactionReference; 	//String (Unique)

        @Enumerated(EnumType.STRING)
        private TransactionType transactionType; 		//String (CREDIT/DEBIT)

        private BigDecimal amount;
        private LocalDateTime transactionDate;
        private String description;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "account_id")
        private BankAccount accountId;
}