package com.bank.dto.bankdto;

import com.bank.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountRequestDto {
      private Integer customerId;
      private AccountType accountType;
      private BigDecimal balance;

}

