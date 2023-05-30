package com.example.webapi.controller;

import com.example.webapi.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){ // inject service layer
        this.transactionService = transactionService;
    }


    /**
     * The RESTful API, url: http://localhost:8080/1001/result
     * This API will return the final result.
     * @param customerId Id of customer
     * @return ReponseEntity wrapped TransactionResultDTO
     */
    @GetMapping("/{customerId}/result")
    public ResponseEntity<?> getResult(@PathVariable Long customerId){
        log.info("Controller Layer: getResult()");
        try {
            ResponseEntity<?> res = new ResponseEntity<>(transactionService.getResult(customerId), HttpStatus.OK);
            log.info("Controller Layer: returning response body");
            return res;
        } catch (Exception e){
            log.error("Controller Layer Error!", e);
            return new ResponseEntity<>("ERROR!", HttpStatus.BAD_REQUEST);
        }
    }



}



