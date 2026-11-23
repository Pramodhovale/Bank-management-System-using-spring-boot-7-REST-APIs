package com.bank.dto.trandto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionAccountRequestDto {
    private BigDecimal amount;
    private String description;
}
