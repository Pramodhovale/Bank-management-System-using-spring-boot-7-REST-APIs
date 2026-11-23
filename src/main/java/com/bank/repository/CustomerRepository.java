package com.bank.repository;

import com.bank.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByCustomerEmail(String customerEmail);
    boolean existsByMobileNumber(String mobileNumber);

    @EntityGraph(attributePaths = "bankAccounts")
    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
    Optional<Customer> findCustomerWithAccounts(@Param("customerId") Integer customerId);
}
