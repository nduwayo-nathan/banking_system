package com.bank_MS.controller;



import com.bank_MS.exception.EmailException;

import com.bank_MS.model.Banking;
import com.bank_MS.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank_ms")
public class BankingController {

     private final BankingService bankingService;


    @PostMapping("/saving")
    public ResponseEntity<Banking> saving(@RequestBody Banking banking) throws EmailException {
        return bankingService.save(banking);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<Banking> withdrawing(@RequestBody Banking banking) throws EmailException {
        return bankingService.withdraw(banking);
    }

    @PostMapping("/transfer/{toAccount}")
    public ResponseEntity<Banking> transferring(@RequestBody Banking banking, @PathVariable String toAccount) throws EmailException {
        return bankingService.transfer(banking, toAccount);
    }


}
