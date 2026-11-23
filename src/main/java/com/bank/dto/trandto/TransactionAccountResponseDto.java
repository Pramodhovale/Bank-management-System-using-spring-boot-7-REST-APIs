package com.bank.dto.trandto;

import com.bank.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionAccountResponseDto {
    private Integer accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal availableBalance;
    private String  message;
}