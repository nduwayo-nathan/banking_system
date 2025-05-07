package com.bank_MS.repository;

import com.bank_MS.model.Banking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingRepo extends JpaRepository<Banking, Integer> {
}
