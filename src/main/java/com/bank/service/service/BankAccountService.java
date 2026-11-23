package com.bank.service.service;

import com.bank.dto.bankdto.BankAccountRequestDto;
import com.bank.dto.bankdto.BankAccountResponseDto;
import com.bank.dto.trandto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface BankAccountService  {

    BankAccountResponseDto openCusAccount(@Valid BankAccountRequestDto bankAccountRequestDto);

    TransactionAccountResponseDto depositMoney(Integer accountId, TransactionAccountRequestDto requestDto);

    TransactionAccountResponseDto withDrawMoney(Integer accountId, TransactionAccountRequestDto transactionAccountRequestDto);

    TransferMoneyResponseDto transferMoneyBetweenAccounts(TransferMoneyRequestDto transferMoneyRequetsDto);

    List<TransactionHistoryDto> findByIdTransactionHistory(Integer accountId);
}