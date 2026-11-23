package com.bank.dto.customeraccount;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAccountResponseDto {
    private Integer customerId;
    private String customerName;
    private String email;
    private List<AccountDetailsDto> accounts;
}
