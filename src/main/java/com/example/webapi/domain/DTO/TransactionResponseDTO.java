package com.example.webapi.domain.DTO;

import com.example.webapi.domain.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;
import java.util.HashMap;

/**
 * This entity carry data from repository layer to upper layers
 */
@Data
@AllArgsConstructor
public class TransactionResponseDTO {
    private String customerName;
    private String purchaseMonth;
    private int points;
    private HashMap<Integer, String> monthMap= new HashMap<>(); // {1:"Jan",2:"Feb",3:"Mar" ..........}
    private void initalMonthMap(){ //make the month map
        String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (int i=1;i<13;i++){
            monthMap.put(i,months[i-1]);
        }
    }

    public TransactionResponseDTO(Transaction trs){
        initalMonthMap();// create monthMap
        this.customerName = trs.getCustomerName();
        //convert month from Integer to String: 2 -> "Feb"
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trs.getPurchaseDate());

        this.purchaseMonth = monthMap.get(calendar.get(Calendar.MONTH) + 1); //convert "2020-05-26" to "May"
        this.points = trs.getPoints();
    }

}
