package com.bank.dto.customerdto;

import lombok.Data;

@Data
public class CustomerResponseDto {
    private Integer customerId;
    private String  customerName;
    private String  customerEmail;
    private String  customerMessage;
}