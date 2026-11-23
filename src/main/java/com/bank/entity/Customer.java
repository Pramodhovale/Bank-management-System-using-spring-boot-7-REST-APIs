package com.bank.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Customer extends BaseEntity{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer customerId;
        private String  customerName;

        @Column(unique = true)
        private String  customerEmail;
        private String  mobileNumber;
        private String  customerAddress;

        @OneToMany(mappedBy = "customerId",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY)
        private List<BankAccount> bankAccounts;
}

