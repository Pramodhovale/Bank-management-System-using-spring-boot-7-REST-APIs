package com.bank.dto.customeraccount;

import com.bank.enums.AccountType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountDetailsDto {
        private Integer accountId;
        private String accountNumber;
        private AccountType accountType;
        private BigDecimal balance;
}
