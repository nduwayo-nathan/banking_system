package com.bank_MS.service;



import com.bank_MS.enums.EmailService;
import com.bank_MS.enums.TransactionType;
import com.bank_MS.exception.EmailException;
import com.bank_MS.model.Banking;
import com.bank_MS.model.Customer;
import com.bank_MS.model.Message;
import com.bank_MS.repository.BankingRepo;
import com.bank_MS.repository.CustomerRepo;
import com.bank_MS.repository.MessageRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final BankingRepo bankingRepo;
    private final CustomerService customerService;
    private final CustomerRepo customerRepo;
    private final MessageRepo messageRepo;
    private  final MessageServices messageServices;
    private final   EmailService emailService;


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ResponseEntity<Banking> save(Banking banking) throws EmailException {
        Customer customer = customerService.getCustomer();

               customer.setBalance( customer.getBalance().add(banking.getAmount()));
               customer.setLastUpdateDateTime(LocalDateTime.now());
               customerRepo.save(customer);

               banking.setBankingDateTime(LocalDateTime.now());
               banking.setAccount(customer.getAccount());
               banking.setCustomer(customer);
               banking.setType(TransactionType.SAVING);
               bankingRepo.save(banking);

               sendTransactionNotification(customer,"SAVING",banking.getAmount(),banking.getAccount());


               return new ResponseEntity<>(banking,HttpStatus.OK);

    }


    @Transactional
    public ResponseEntity<Banking> withdraw(Banking banking) throws EmailException {

        Customer customer=customerService.getCustomer();

        if (customer.getBalance().compareTo(banking.getAmount())<0){
            throw new IllegalArgumentException("Insufficient funds");
        }

        customer.setBalance( customer.getBalance().subtract(banking.getAmount()));
        customer.setLastUpdateDateTime(LocalDateTime.now());
        customerRepo.save(customer);
        banking.setBankingDateTime(LocalDateTime.now());
        banking.setType(TransactionType.WITHDRAW);
        banking.setCustomer(customer);
        banking.setAccount(customer.getAccount());
        bankingRepo.save(banking);

        sendTransactionNotification(customer,"WITHDRAW",banking.getAmount(),banking.getAccount());


        return new ResponseEntity<>(banking,HttpStatus.OK);


    }


    @Transactional
    public ResponseEntity<Banking> transfer(Banking banking, String toAccount) throws EmailException {

        Customer fromCustomer = customerService.getCustomer();
        Customer toCustomer = customerRepo.findCustomerByAccount(toAccount);


        if (fromCustomer.getBalance().compareTo(toCustomer.getBalance()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        fromCustomer.setBalance(fromCustomer.getBalance().subtract(banking.getAmount()));
        fromCustomer.setLastUpdateDateTime(LocalDateTime.now());
        customerRepo.save(fromCustomer);

        toCustomer.setBalance(toCustomer.getBalance().add(banking.getAmount()));
        toCustomer.setLastUpdateDateTime(LocalDateTime.now());
        customerRepo.save(toCustomer);

        banking.setBankingDateTime(LocalDateTime.now());
        banking.setType(TransactionType.TRANSFER);
        banking.setAccount(fromCustomer.getAccount());
        banking.setCustomer(fromCustomer);


        sendTransactionNotification(fromCustomer, "Transfer (sent)", banking.getAmount(), banking.getAccount());
        sendTransactionNotification(toCustomer, "Transfer (received)", banking.getAmount(), banking.getAccount());

        return new ResponseEntity<>(bankingRepo.save(banking),HttpStatus.OK);


    }


    @Transactional
    protected void sendTransactionNotification(Customer customer, String type, BigDecimal amount, String
            account) throws EmailException {

            String messageContent = String.format(
                    "Dear %s %s, Your %s of %s on your account %s has been Completed Successfully.",
                    customer.getFirstName(),
                    customer.getLastName(),
                    type,
                    amount,
                    account

            );

            Message message = new Message();
            message.setCustomer(customer);
            message.setMessage(messageContent);
            message.setDateTime(LocalDateTime.now());
            messageRepo.save(message);

            if (type.equals("transfer")) {}
            emailService.sendEmail(
                    customer.getEmail(),
                    "Transaction notification",
                    messageContent
            );

        }
    }
