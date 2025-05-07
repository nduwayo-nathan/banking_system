package com.bank_MS.controller;

import com.bank_MS.model.Customer;
import com.bank_MS.model.LoginRequest;
import com.bank_MS.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank_ms")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public String welcome() {
        return customerService.welcome();
    }
    @PostMapping("/register")
    public Customer registerCustomer(@RequestBody @Valid Customer customer) {
       return customerService.register(customer);
    }
    @PostMapping("/login")
    public String loginCustomer(@RequestBody @Valid LoginRequest loginRequest) {
        return customerService.verify(loginRequest);
    }
}
