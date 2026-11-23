package com.bank.service;

import com.bank.dto.customeraccount.AccountDetailsDto;
import com.bank.dto.customeraccount.CustomerAccountResponseDto;
import com.bank.dto.customerdto.CustomerRequestDto;
import com.bank.dto.customerdto.CustomerResponseDto;
import com.bank.entity.BankAccount;
import com.bank.entity.Customer;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.exceptions.DuplicateCustomerException;
import com.bank.repository.CustomerRepository;
import com.bank.service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomerResponseDto registerNewAccount(CustomerRequestDto requestDto) {

        if(customerRepository.existsByCustomerEmail(requestDto.getCustomerEmail())) {
            throw new DuplicateCustomerException("Duplicate Email Is Created");
        }

        if(customerRepository.existsByMobileNumber(requestDto.getMobileNumber())) {
            throw new DuplicateCustomerException("Duplicate Mobile Number Is Created");
        }
        Customer customer = new Customer();
                customer.setCustomerName(requestDto.getCustomerName());
                customer.setCustomerEmail(requestDto.getCustomerEmail());
                customer.setMobileNumber(requestDto.getMobileNumber());
                customer.setCustomerAddress(requestDto.getCustomerAddress());

                Customer save = customerRepository.save(customer);

        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
                    customerResponseDto.setCustomerId(save.getCustomerId());
                    customerResponseDto.setCustomerEmail(save.getCustomerEmail());
                    customerResponseDto.setCustomerName(save.getCustomerName());
                    customerResponseDto.setCustomerMessage("Customer registeed sccussfully");
        return customerResponseDto;
    }

    @Override
    public CustomerAccountResponseDto
            retrievingCustomerDetailsAndAccounts(Integer customerId) {
        Optional<Customer> customerWithAccounts = customerRepository.findCustomerWithAccounts(customerId);

        if(customerWithAccounts.isEmpty()){
            throw new CustomerNotFoundException("Customer not found!!");
        }
        // Customer mapping
        Customer customerEntity = customerWithAccounts.get();
        CustomerAccountResponseDto response = new CustomerAccountResponseDto();

        response.setCustomerId(customerEntity.getCustomerId());
        response.setCustomerName(customerEntity.getCustomerName());
        response.setEmail(customerEntity.getCustomerEmail());

        //AccountMapping
        List<AccountDetailsDto> accountDetails = new ArrayList<>();

        for(BankAccount bankAccount: customerEntity.getBankAccounts()){
            AccountDetailsDto accountDetailsDto = new AccountDetailsDto();

            accountDetailsDto.setAccountId(bankAccount.getAccountId());
            accountDetailsDto.setAccountNumber(bankAccount.getAccountNumber());
            accountDetailsDto.setAccountType(bankAccount.getAccountType());
            accountDetailsDto.setBalance(bankAccount.getBalance());

            accountDetails.add(accountDetailsDto);
        }
        response.setAccounts(accountDetails);
        return response;
    }

}
