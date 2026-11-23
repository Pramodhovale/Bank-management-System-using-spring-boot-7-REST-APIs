package com.bank.dto.trandto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferMoneyResponseDto {
    private Integer fromAccountId;
    private Integer toAccountId;
    private BigDecimal transferAmount;
    private BigDecimal senderBalance;
    private BigDecimal receiverBalance;
    private String message;
}

