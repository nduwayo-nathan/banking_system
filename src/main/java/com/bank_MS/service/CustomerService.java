package com.bank_MS.service;


import com.bank_MS.model.Customer;
import com.bank_MS.model.LoginRequest;
import com.bank_MS.repository.CustomerRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final HttpServletRequest request;

    public Customer getCustomer() {
        final String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        final String token = header.substring(7);
        String account=jwtService.extractAccount(token);

        Customer customer=customerRepo.findCustomerByAccount(account);

        return customer;

    }
    public String welcome() {
       Customer customer= getCustomer();

       return "Welcome " + customer.getFirstName() + " " + customer.getLastName();

    }


    private  BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);

    public Customer register(Customer customer) {
        BigDecimal defaultBalance = BigDecimal.ZERO;
        Random random = new Random();
        Long accountNumber = 1000000000L + random.nextInt(900000000);

        customer.setBalance(defaultBalance);
        customer.setLastUpdateDateTime(LocalDateTime.now());
        customer.setAccount(String.valueOf(accountNumber));
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
       return customerRepo.save(customer);
    }


    public String verify(LoginRequest loginRequest) {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getAccount(),loginRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(loginRequest.getAccount());
        }
        return "failure";
    }



}
