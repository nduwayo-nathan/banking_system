package com.bank_MS.repository;

import com.bank_MS.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
 Customer findCustomerByAccount(String account);
}
