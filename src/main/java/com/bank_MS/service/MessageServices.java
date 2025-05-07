package com.bank_MS.service;
import com.bank_MS.model.Message;
import com.bank_MS.repository.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServices {
    private final MessageRepo messageRepo;

    public Message saveMessage(Message message) {
        return  messageRepo.save(message);
    }
}
