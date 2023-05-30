package com.example.webapi.service;

import com.example.webapi.domain.DTO.TransactionResultDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    TransactionResultDTO getResult(Long customerId);
}
