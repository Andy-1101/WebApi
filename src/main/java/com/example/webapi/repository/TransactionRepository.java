package com.example.webapi.repository;

import com.example.webapi.domain.DTO.TransactionResponseDTO;
import com.example.webapi.domain.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TransactionRepository {
    Collection<TransactionResponseDTO> getById(Long customerId);
}
