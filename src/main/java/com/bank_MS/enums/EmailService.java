package com.bank_MS.enums;


import com.bank_MS.exception.EmailException;

public interface EmailService {
    void sendEmail(String to, String subject, String body) throws EmailException;
}
