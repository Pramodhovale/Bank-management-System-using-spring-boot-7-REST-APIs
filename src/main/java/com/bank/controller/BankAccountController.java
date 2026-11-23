package com.bank.controller;

import com.bank.cust_bank_consts.CustToBankConst;
import com.bank.dto.CommonDto;
import com.bank.dto.bankdto.BankAccountRequestDto;
import com.bank.dto.bankdto.BankAccountResponseDto;
import com.bank.dto.customerdto.CustomerRequestDto;
import com.bank.dto.trandto.*;
import com.bank.service.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apib/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    //API 2
    @PostMapping("/accounts")
    public ResponseEntity<CommonDto>
    openAccount(@Valid @RequestBody BankAccountRequestDto bankAccountRequestDto){
        BankAccountResponseDto bAR =  bankAccountService.openCusAccount(bankAccountRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonDto(CustToBankConst.MESSAGE_201,CustToBankConst.STATUS_200,bAR));
    }

    //API 3: Deposit Money
    @PostMapping("/accounts/{accountId}/deposit")
    public  ResponseEntity<CommonDto>
        depositMoney(@PathVariable Integer accountId,
                     @RequestBody TransactionAccountRequestDto requestDto){
        TransactionAccountResponseDto TankRepDto =
                bankAccountService.depositMoney(accountId,requestDto);

      return ResponseEntity
              .status(HttpStatus.OK)
              .body(new CommonDto(  CustToBankConst.MESSAGE_143_UPDATE,
                                    CustToBankConst.STATUS_200,
                      TankRepDto));
    }

    //API 4: Withdraw Money
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<CommonDto> withDrawMoney(
            @PathVariable Integer accountId,
            @RequestBody TransactionAccountRequestDto transactionAccountRequestDto) {
        TransactionAccountResponseDto transactionAccountResponseDto = bankAccountService.withDrawMoney(
                accountId, transactionAccountRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto(CustToBankConst.MESSAGE_143_UPDATE,
                                    CustToBankConst.STATUS_200,
                        transactionAccountResponseDto));
    }

    //API 5: Transfer Money Between Accounts
    @PostMapping("/accounts/transfer")
    public ResponseEntity<CommonDto>
            TransferMoneyBetweenAccounts(@RequestBody TransferMoneyRequestDto transferMoneyRequetsDto){

        TransferMoneyResponseDto responseDto = bankAccountService.transferMoneyBetweenAccounts
               (transferMoneyRequetsDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto(CustToBankConst.STATUS_200,
                                    CustToBankConst.MESSAGE_110,
                        responseDto));
    }


    //API 7: Get Account Transaction History
    @GetMapping("/api/accounts/{accountId}/transactions")
    public ResponseEntity<CommonDto>
            FetchTransactionHistory(@PathVariable Integer accountId){

        List<TransactionHistoryDto> byIdTransactionHistory = bankAccountService.findByIdTransactionHistory(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto(CustToBankConst.STATUS_200,
                                    CustToBankConst.MESSAGE_110,
                            byIdTransactionHistory));
    }
}

