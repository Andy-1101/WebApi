package com.example.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;

@Table(name="Transaction")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId; //primary key of transaction
    private Long customerId; //unique id for each customer
    private String customerName; //customer name, can be duplicated
    private Date purchaseDate; // e.g. "2022-05-26"
    private Double amount; // amount of money spend for the specific transaction
    @Column(insertable=false, updatable=false)
    private Integer points; // virtual column, points earned for that transaction

    /*
    override equals and hashcode methods for comparing obnjects in the future.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return points == that.points && Objects.equals(customerId, that.customerId) && Objects.equals(customerName, that.customerName) && Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, customerName, purchaseDate ,points);
    }
}