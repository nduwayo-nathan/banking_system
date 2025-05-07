package com.bank_MS.service;

import com.bank_MS.model.Customer;
import com.bank_MS.model.CustomerPrincipal;
import com.bank_MS.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailService  implements UserDetailsService {

    private final CustomerRepo customerRepo;


    @Override
    public UserDetails loadUserByUsername(String customerAccount) throws UsernameNotFoundException {
        Customer customer = customerRepo.findCustomerByAccount(customerAccount);

        if (customer == null) {
            System.out.println("Customer not found");
            throw new UsernameNotFoundException("Customer not found");
        }
        return new CustomerPrincipal(customer);
    }
}
