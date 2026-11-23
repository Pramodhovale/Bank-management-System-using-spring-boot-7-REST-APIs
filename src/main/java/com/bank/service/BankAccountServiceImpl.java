package com.bank.service;

import com.bank.dto.bankdto.BankAccountRequestDto;
import com.bank.dto.bankdto.BankAccountResponseDto;
import com.bank.dto.trandto.*;
import com.bank.entity.BankAccount;
import com.bank.entity.BankTransaction;
import com.bank.entity.Customer;
import com.bank.enums.AccountStatus;
import com.bank.enums.TransactionType;
import com.bank.exceptions.InsufficientBalanceException;
import com.bank.exceptions.InvalidAccountException;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.BankTransactionRepository;
import com.bank.repository.CustomerRepository;
import com.bank.service.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final BankTransactionRepository bankTransactionRepository;

    @Override
    public BankAccountResponseDto
    openCusAccount(BankAccountRequestDto bankAccountRequestDto) {

        Optional<Customer> byId =
                customerRepository.findById(bankAccountRequestDto.getCustomerId());

        if (byId.isEmpty()) {
            throw new InvalidAccountException("Customer not found");
        }

        Customer customer = byId.get();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomerId(customer);
        bankAccount.setAccountType(bankAccountRequestDto.getAccountType());
        bankAccount.setBalance(bankAccountRequestDto.getBalance());
        bankAccount.setAccountStatus(AccountStatus.ACTIVE);
        bankAccount.setCreatedAt(LocalDateTime.now());

        bankAccount.setAccountNumber("ACC" + System.currentTimeMillis());
        BankAccount savedAccount = bankAccountRepository.save(bankAccount);

        BankAccountResponseDto responseDto = new BankAccountResponseDto();

        responseDto.setAccountId(savedAccount.getAccountId());
        responseDto.setAccountNumber(savedAccount.getAccountNumber());
        responseDto.setAccountType(savedAccount.getAccountType());
        responseDto.setBalance(savedAccount.getBalance());
        responseDto.setAccountStatus(savedAccount.getAccountStatus());
        responseDto.setCustomerId(customer.getCustomerId());
        responseDto.setMessage("Bank account opened successfully");

        return responseDto;
    }

    @Override
    @Transactional
    public TransactionAccountResponseDto depositMoney(
            Integer accountId,
            TransactionAccountRequestDto requestDto) {

        // Step 1: Find the existing bank account
        Optional<BankAccount> byId =
                bankAccountRepository.findById(accountId);

        // Step 2: Check whether the account exists
        if (byId.isEmpty()) {
            throw new InvalidAccountException("Account not found");
        }

        // Step 3: Get the existing account
        BankAccount bankAccount = byId.get();

        // Step 4: Check whether the account is active
        if (bankAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountException("Account is not active");
        }

        // Step 5: Validate the deposit amount
        if (requestDto.getAmount() == null ||
                requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero");
        }

        // Step 6: Calculate the new balance
        BigDecimal newBalance = bankAccount.getBalance()
                .add(requestDto.getAmount());

        // Step 7: Update the account balance
        bankAccount.setBalance(newBalance);

        // Step 8: Save the updated account
        BankAccount savedAccount =
                bankAccountRepository.save(bankAccount);

        // Step 9: Create the transaction record
        BankTransaction transaction = new BankTransaction();

        transaction.setTransactionReference(UUID.randomUUID().toString());
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setAmount(requestDto.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(requestDto.getDescription());
        transaction.setAccountId(savedAccount);

        // Step 10: Save the transaction record
        bankTransactionRepository.save(transaction);

        // Step 11: Prepare the response DTO
        TransactionAccountResponseDto responseDto =
                new TransactionAccountResponseDto();

        responseDto.setAccountId(savedAccount.getAccountId());
        responseDto.setTransactionType(TransactionType.CREDIT);
        responseDto.setAmount(requestDto.getAmount());
        responseDto.setAvailableBalance(savedAccount.getBalance());
        responseDto.setMessage("Amount deposited successfully");

        return responseDto;
    }

    @Override
    @Transactional
    public TransactionAccountResponseDto withDrawMoney(
            Integer accountId,
            TransactionAccountRequestDto transactionAccountRequestDto) {

        Optional<BankAccount> byId = bankAccountRepository.findById(accountId);
        if (byId.isEmpty()) {
            throw new InvalidAccountException("Account doen't exist");
        }
        BankAccount bankAccount = byId.get();

        if (bankAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountException("Account is not active");
        }

        if (transactionAccountRequestDto.getAmount() == null ||
                transactionAccountRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        if (transactionAccountRequestDto.getAmount().compareTo(bankAccount.getBalance())>0) {
            throw new InsufficientBalanceException("insufficient balance");
        }

        bankAccount.setBalance(bankAccount.getBalance().subtract(
                        transactionAccountRequestDto.getAmount()));

        bankAccountRepository.save(bankAccount);

        BankTransaction transaction1 = new BankTransaction();
                transaction1.setTransactionReference(UUID.randomUUID().toString());
                transaction1.setTransactionType(TransactionType.DEBIT);
                transaction1.setAmount(transactionAccountRequestDto.getAmount());
                transaction1.setTransactionDate(LocalDateTime.now());
                transaction1.setDescription(transactionAccountRequestDto.getDescription());
                transaction1.setAccountId(bankAccount);

                bankTransactionRepository.save(transaction1);

        TransactionAccountResponseDto TARD  = new TransactionAccountResponseDto();

        TARD.setAccountId(bankAccount.getAccountId());
        TARD.setAvailableBalance(bankAccount.getBalance());
        TARD.setAmount(transactionAccountRequestDto.getAmount());
        TARD.setMessage("Amount withdrawn successfully");
        TARD.setTransactionType(TransactionType.DEBIT);
        return TARD;
    }

    @Override
    @Transactional
    public TransferMoneyResponseDto transferMoneyBetweenAccounts(
            TransferMoneyRequestDto transferMoneyRequestDto) {

        Optional<BankAccount> sender = bankAccountRepository.findByIdForUpdate(
                transferMoneyRequestDto.getFromAccountId());

        if(sender.isEmpty()) {
              throw new InvalidAccountException("The account not found");
        }

        Optional<BankAccount> receiver = bankAccountRepository.findByIdForUpdate(
                transferMoneyRequestDto.getToAccountId());

        if(receiver.isEmpty()){
            throw new InvalidAccountException("The account not found");
        }

        BankAccount sendBankAccount = sender.get();
        BankAccount recBankAccount = receiver.get();

        if(sendBankAccount.getAccountId().equals(recBankAccount.getAccountId())){
            throw  new IllegalArgumentException("Sender and receiver accounts must be different.");
        }

        if(sendBankAccount.getAccountStatus() != AccountStatus.ACTIVE){
            throw new InvalidAccountException("Account not found");
        }

        if(recBankAccount.getAccountStatus() != AccountStatus.ACTIVE){
            throw new InvalidAccountException("Account not found");
        }

        if(transferMoneyRequestDto.getAmount()==null ||
                transferMoneyRequestDto.getAmount().compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Amount is Insufficient");
        }

        if (transferMoneyRequestDto.getAmount()
                .compareTo(sendBankAccount.getBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sendBankAccount.setBalance(sendBankAccount.getBalance()
                .subtract(transferMoneyRequestDto.getAmount()));

        recBankAccount.setBalance(recBankAccount.getBalance()
                .add(transferMoneyRequestDto.getAmount()));

        bankAccountRepository.save(sendBankAccount);
        bankAccountRepository.save(recBankAccount);

        BankTransaction senderT = new BankTransaction();
        senderT.setTransactionType(TransactionType.DEBIT);
        senderT.setAmount(transferMoneyRequestDto.getAmount());
        senderT.setDescription("Amount is transferred");
        senderT.setTransactionReference(UUID.randomUUID().toString());
        senderT.setAccountId(sendBankAccount);
        senderT.setTransactionDate(java.time.LocalDateTime.now());
        bankTransactionRepository.save(senderT);

        BankTransaction receiverT = new BankTransaction();
        receiverT.setTransactionType(TransactionType.CREDIT);
        receiverT.setAmount(transferMoneyRequestDto.getAmount());
        receiverT.setDescription("Amount is received");
        receiverT.setTransactionReference(UUID.randomUUID().toString());
        receiverT.setTransactionDate(LocalDateTime.now());
        receiverT.setAccountId(recBankAccount);
        bankTransactionRepository.save(receiverT);

        TransferMoneyResponseDto response = new TransferMoneyResponseDto();

        response.setFromAccountId(sendBankAccount.getAccountId());
        response.setToAccountId(recBankAccount.getAccountId());
        response.setTransferAmount(transferMoneyRequestDto.getAmount());
        response.setSenderBalance(sendBankAccount.getBalance());
        response.setReceiverBalance(recBankAccount.getBalance());
        response.setMessage("Fund transferred successfully");

        return response;
    }

    @Override
    public List<TransactionHistoryDto> findByIdTransactionHistory(Integer accountId) {

        Optional<BankAccount> byId = bankAccountRepository.findById(accountId);

        if(byId.isEmpty()){
            throw new RuntimeException("Account not found");
        }

        List<BankTransaction> transaction =
                bankTransactionRepository
                        .findByAccountId_AccountIdOrderByTransactionDateDesc(accountId);

        List<TransactionHistoryDto> result = new ArrayList<>();

        for(BankTransaction bankTransaction1:transaction){
            TransactionHistoryDto dto = new TransactionHistoryDto();

            dto.setTransactionId(bankTransaction1.getTransactionId());
            dto.setTransactionReference(bankTransaction1.getTransactionReference());
            dto.setTransactionType(bankTransaction1.getTransactionType());
            dto.setAmount(bankTransaction1.getAmount());
            dto.setTransactionDate(bankTransaction1.getTransactionDate());
            dto.setDescription(bankTransaction1.getDescription());

            result.add(dto);
        }
        return result;
    }
}