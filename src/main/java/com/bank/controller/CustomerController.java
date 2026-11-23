package com.bank.controller;

import com.bank.cust_bank_consts.CustToBankConst;
import com.bank.dto.CommonDto;
import com.bank.dto.customeraccount.CustomerAccountResponseDto;
import com.bank.dto.customerdto.CustomerRequestDto;
import com.bank.dto.customerdto.CustomerResponseDto;
import com.bank.service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apic/customer")
@RequiredArgsConstructor
public class CustomerController {

        private final CustomerService customerService;

        @PostMapping("/register")
        public ResponseEntity<CommonDto>
        register(@Valid @RequestBody CustomerRequestDto requestDto) {

            CustomerResponseDto responseDto = customerService.registerNewAccount(requestDto);

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonDto(
                            CustToBankConst.STATUS_200,
                            CustToBankConst.MESSAGE_201,
                            responseDto
                    ));
        }

        //API 6: Get Customer Account Details
        @GetMapping("/GET/{customerId}/accounts")
        public ResponseEntity<CommonDto>
        getCustomerAccountDetails(@PathVariable Integer customerId){

            CustomerAccountResponseDto customerAccountResponseDto = customerService.retrievingCustomerDetailsAndAccounts(customerId);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto(
                        CustToBankConst.STATUS_200,
                        CustToBankConst.MESSAGE_201,
                        customerAccountResponseDto
                ));
    }


}