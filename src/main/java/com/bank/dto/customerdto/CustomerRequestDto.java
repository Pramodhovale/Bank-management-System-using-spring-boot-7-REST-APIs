package com.bank.dto.customerdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequestDto {
    @NotBlank(message = "Customer name is required")
    @Size(min = 3, max = 50,
            message = "Customer name must be between 3 and 50 characters")
    private String  customerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String  customerEmail;

    @NotBlank(message = "Mobile number is required")
    private String  mobileNumber;

    @NotBlank(message = "Address is required")
    private String  customerAddress;

    public CustomerRequestDto(String status200, String message201, CustomerResponseDto responseDto) {
    }
}