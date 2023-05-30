package com.example.webapi.domain.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * This DTO carry the final result to controller.
 */
@Data
public class TransactionResultDTO {
    private Map<String, Integer> monthlyPoints; // {"Jan":500,"Jun":75998}
    private Integer totalPoints;

    public TransactionResultDTO(Map<String,Integer> monthlyPoints, Integer totalPoints){
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

}
