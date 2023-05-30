package com.example.webapi.repository;
import com.example.webapi.domain.DTO.TransactionResponseDTO;
import com.example.webapi.domain.entity.Transaction;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository{

    private static final Logger log = LoggerFactory.getLogger(TransactionRepositoryImpl.class);
    private final EntityManager conn;

    @Autowired
    public TransactionRepositoryImpl(EntityManager conn){ // constructor dependency inject -> inject database connection
        this.conn = conn;
    } // Inject datasource




    /**
     * This is the core CRUD method, because all APIs will use data retried by customerId
     * this is the only repository method.
     * The method filter data only 3 months before today by HQL.
     * @param customerId input customerId
     * @return all transaction objects related to the customerId
     */
    @Override
    public Collection<TransactionResponseDTO> getById(Long customerId) {
        log.info("Repository layer: getById()");
        try {
            log.info("Repository layer: executing HQL");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //get current date
            Date date = new Date();
            String d = formatter.format(date);
            java.sql.Date toDate = new java.sql.Date(formatter.parse(d).getTime());

            // get date before 3 months
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, -3);
            String d2 = formatter.format(c.getTime());
            java.sql.Date fromDate = new java.sql.Date(formatter.parse(d2).getTime());

            //CRUD operations
            Collection<Transaction> transactionObjs = conn.createQuery("From Transaction t WHERE t.customerId =:customer_id and t.purchaseDate between :fromDate and :endDate") // connect to databse and execute HQL
                    .setParameter("customer_id", customerId)
                    .setParameter("fromDate", fromDate)
                    .setParameter("endDate", toDate)
                    .getResultList();
            log.info("Repository layer: HQL done, returning DTOs to service layer");
            return transactionObjs // convert Transaction objects to DTO, DTO carry data to upper layer
                    .stream()
                    .map(TransactionResponseDTO::new)
                    .collect(Collectors.toList());

        } catch (Exception e){
            log.error("Repository error!", e);
            throw new RuntimeException();
        }
    }
}
