package com.bank.dto.trandto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferMoneyRequestDto {
    private Integer fromAccountId;
    private Integer toAccountId;
    private BigDecimal amount;
    private String description;
}
