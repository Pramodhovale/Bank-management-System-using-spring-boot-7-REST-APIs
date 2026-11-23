package com.bank.dto.bankdto;

import com.bank.enums.AccountStatus;
import com.bank.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountResponseDto {
    private Integer accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus accountStatus;
    private String message;
    private Integer customerId;
}