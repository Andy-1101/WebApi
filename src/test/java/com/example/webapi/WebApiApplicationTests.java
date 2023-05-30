package com.example.webapi;

import com.example.webapi.controller.TransactionController;
import com.example.webapi.domain.DTO.TransactionResponseDTO;
import com.example.webapi.domain.DTO.TransactionResultDTO;
import com.example.webapi.domain.entity.Transaction;
import com.example.webapi.repository.TransactionRepository;
import com.example.webapi.repository.TransactionRepositoryImpl;
import com.example.webapi.service.TransactionService;
import com.example.webapi.service.TransactionServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class WebApiApplicationTests {

    private static TransactionServiceImpl service;
    @Autowired
    private TransactionRepositoryImpl repository;
    private static TransactionRepository transactionRepositoryMock;
    private static TransactionController transactionController;
    private static TransactionService transactionServiceMock;

    /**
     * set up all mocks before run any tests
     */
    @BeforeAll
    public static void init(){
        // use repository mock to test service layer
        transactionRepositoryMock =  Mockito.mock(TransactionRepository.class);
        service = new TransactionServiceImpl(transactionRepositoryMock);

        // use service mock to test repository layer
        transactionServiceMock = Mockito.mock(TransactionService.class);
        transactionController = new TransactionController(transactionServiceMock);
    }

    /**
     * This method test the function of repository,
     * The test fetch data from database and compare with desired outcome.
     * @throws ParseException
     */
    @Test
    public void repositoryTest() throws ParseException {
        // forge customerIds for tests
        Long customerId1 = 1004L; // 1 records
        Long customerId2 = 1005L; // 2 records
        Long customerId3 = 4555555L; // no records
        Collection<TransactionResponseDTO> result1 = repository.getById(customerId1);
        Collection<TransactionResponseDTO> result2 = repository.getById(customerId2);
        Collection<TransactionResponseDTO> result3 = repository.getById(customerId3);

        // data retrieve
        Assert.assertTrue(!result1.isEmpty());
        Assert.assertTrue(!result2.isEmpty());
        Assert.assertTrue(result3.isEmpty());

        //Data Test: test the correctness of result1
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Collection<TransactionResponseDTO> compareList1 = new ArrayList<>(); // forge a list for comparing with test result

        //create a transaction
        Transaction transaction1 = new Transaction();
        String str = "2020-05-01";
        java.util.Date d = formatter.parse(str);
        java.sql.Date date = new java.sql.Date(d.getTime());
        transaction1.setAmount(110.8);
        transaction1.setPurchaseDate(date);
        transaction1.setCustomerId(1004L);
        transaction1.setCustomerName("Ross");
        transaction1.setPoints(72);
        compareList1.add(new TransactionResponseDTO(transaction1));
        Assert.assertEquals(result1, compareList1); // compare data from database with the predicted result

        //Data Test: test the correctness of result2
        Collection<TransactionResponseDTO> compareList2 = new ArrayList<>();

        //create 2 transactions
        Transaction transaction2 = new Transaction();
        Transaction transaction3 = new Transaction();
        String str2 = "2023-04-08";
        String str3 = "2023-04-29";

        java.util.Date d2 = formatter.parse(str2);
        java.sql.Date date2 = new java.sql.Date(d2.getTime());

        java.util.Date d3 = formatter.parse(str3);
        java.sql.Date date3 = new java.sql.Date(d3.getTime());

        transaction2.setAmount(15.8);
        transaction2.setPurchaseDate(date2);
        transaction2.setCustomerId(1005L);
        transaction2.setCustomerName("Richel");
        transaction2.setPoints(0);
        compareList2.add(new TransactionResponseDTO(transaction2));

        transaction3.setAmount(1137.89);
        transaction3.setPurchaseDate(date3);
        transaction3.setCustomerId(1005L);
        transaction3.setCustomerName("Richel");
        transaction3.setPoints(2126);
        compareList2.add(new TransactionResponseDTO(transaction3));

        Assert.assertEquals(result2,compareList2);
    }

    /**
     * This is the first test for service, this test will find out if the service can handle
     * customerId not in the database yet
     */
    @Test
    public void serviceTest1(){
        when(transactionRepositoryMock.getById(10L)).thenReturn(new ArrayList<>());

        //Test for empty transactions
        TransactionResultDTO test1 = new TransactionResultDTO(new HashMap<String, Integer>(),0);
        Assert.assertEquals(service.getResult(10L), test1);

    }

    /**
     * This is the second test for service, this test will find out if the service could
     * handle the business logic successfully.
     * @throws ParseException
     */
    @Test
    public void serviceTest2() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        //create test set for testing
        Collection<TransactionResponseDTO> testSet1 = new ArrayList<>();
        Transaction tr1 = new Transaction();
        tr1.setPoints(30);
        tr1.setCustomerName("Andrew");
        tr1.setPurchaseDate(new java.sql.Date(formatter.parse("2020-06-24").getTime()));

        Transaction tr2 = new Transaction();
        tr2.setPoints(90);
        tr2.setCustomerName("Andrew");
        tr2.setPurchaseDate(new java.sql.Date(formatter.parse("2020-06-11").getTime()));

        Transaction tr3 = new Transaction();
        tr3.setPoints(100);
        tr3.setCustomerName("Andrew");
        tr3.setPurchaseDate(new java.sql.Date(formatter.parse("2020-08-08").getTime()));
        TransactionResponseDTO tDto1 = new TransactionResponseDTO(tr1);
        TransactionResponseDTO tDto2 = new TransactionResponseDTO(tr2);
        TransactionResponseDTO tDto3 = new TransactionResponseDTO(tr3);

        //add DTOs into test set
        testSet1.add(tDto1);
        testSet1.add(tDto2);
        testSet1.add(tDto3);

        when(transactionRepositoryMock.getById(1111L)).thenReturn(testSet1);

        // Test for business logic
        TransactionResultDTO compareDTO = new TransactionResultDTO(Map.of("Jun",120,"Aug",100),220);
        Assert.assertEquals(service.getResult(1111L), compareDTO);

    }

    /**
     * Test for controller, controller will receive a forged DTO from Mocked service
     * layer and return a response entity to find out whether the controller can run successfully.
     */
    @Test
    public void controllerTest() {
        HashMap<String, Integer> testMap = new HashMap<>();
        testMap.put("Jan",45);
        testMap.put("Aug", 455);
        TransactionResultDTO testDTO = new TransactionResultDTO(testMap, 900);
        when(transactionServiceMock.getResult(1111L)).thenReturn(testDTO);

        ResponseEntity<?> result = transactionController.getResult(1111L);
        Assert.assertEquals(result, new ResponseEntity<>(testDTO, HttpStatus.OK));
    }

}
