package com.MoneyManager.Dto;

import com.MoneyManager.Enums.Division;
import com.MoneyManager.Enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class TransactionDto {
    private String id;

    private TransactionType type;

    private double amount;

    private String description;

    private String category;

    private Division division;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



}

