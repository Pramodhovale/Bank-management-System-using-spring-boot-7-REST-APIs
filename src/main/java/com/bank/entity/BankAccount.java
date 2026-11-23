package com.bank.entity;

import com.bank.enums.AccountStatus;
import com.bank.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class BankAccount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType; 		//String (SAVINGS/CURRENT)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;    //String (ACTIVE/BLOCKED/CLOSED)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customerId;    	    //FK

    @OneToMany(mappedBy = "accountId",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    private List<BankTransaction> bankTransactions;
}