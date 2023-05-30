package com.example.webapi.service;

import com.example.webapi.domain.DTO.TransactionResultDTO;
import com.example.webapi.domain.DTO.TransactionResponseDTO;
import com.example.webapi.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) { // constructor dependency injection -> inject repository
        this.transactionRepository = transactionRepository;
    }

    /**
     * The service method calculates the total points by accumulating all points as an Integer
     * Then the service method calculates points for each month by using grouping by function of stream api.
     * Finally, construct a new TransactionResultDTO and use that to transfer data to controller layer.
     * @param customerId the ID of customer, customer can use the ID to retrieve data for certain customer.
     * @return TransactionResponseDTO
     */
    @Override
    public TransactionResultDTO getResult(Long customerId) {
        log.info("Service layer: getResult()");

        try{

            Collection<TransactionResponseDTO> transactions = transactionRepository.getById(customerId); // get data from repository
            log.info("Service Layer: Data received from Repository");
            Integer totalPoints = transactions.stream().mapToInt(TransactionResponseDTO::getPoints).sum(); // calculate sum of points for the customer

            Map<String, Integer> res = transactions//group by month and calculate sum of points for each month
                    .stream()
                    .collect(Collectors.groupingBy(TransactionResponseDTO::getPurchaseMonth, Collectors.summingInt(TransactionResponseDTO::getPoints)));
            log.info("Service Layer: calculated points for each month");

            return new TransactionResultDTO(res,totalPoints);

        } catch (Exception e){ //if there is any exception print out error and propagate exception to upper layer
            log.error("Service Layer error!", e);
            throw new RuntimeException();
        }
    }








}


