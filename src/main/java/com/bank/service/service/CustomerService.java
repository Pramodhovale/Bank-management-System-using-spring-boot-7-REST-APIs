package com.bank.service.service;

import com.bank.dto.customeraccount.CustomerAccountResponseDto;
import com.bank.dto.customerdto.CustomerRequestDto;
import com.bank.dto.customerdto.CustomerResponseDto;
import jakarta.validation.Valid;

public interface CustomerService {
    CustomerResponseDto registerNewAccount(@Valid CustomerRequestDto requestDto);
    CustomerAccountResponseDto retrievingCustomerDetailsAndAccounts(Integer customerId);
}
