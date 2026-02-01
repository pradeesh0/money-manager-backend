package com.MoneyManager.Model;

import com.MoneyManager.Enums.Division;
import com.MoneyManager.Enums.TransactionType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
public class Transaction {

    @Id
    private String id;

    private TransactionType type;

    private double amount;

    private String description;

    private String category;

    private Division division;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String accountId;
}
