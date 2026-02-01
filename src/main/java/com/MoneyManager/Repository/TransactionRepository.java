package com.MoneyManager.Repository;

import com.MoneyManager.Enums.Division;
import com.MoneyManager.Enums.TransactionType;
import com.MoneyManager.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Page<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { createdAt: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$category', total: { $sum: '$amount' } } }"
    })
    List<Map<String,Object>> categorySummary(LocalDateTime start, LocalDateTime end);

    Page<Transaction> findAll(Pageable pageable);


    Page<Transaction> findByCategory(String category, Pageable p);
    Page<Transaction> findByDivision(String division, Pageable p);
    Page<Transaction> findByCategoryAndDivision(String category, String division, Pageable p);

    Page<Transaction> findByCreatedAtBetweenAndCategory(LocalDateTime start, LocalDateTime end, String category, Pageable p);

    Page<Transaction> findByCreatedAtBetweenAndDivision(LocalDateTime start, LocalDateTime end, String division, Pageable p);

    Page<Transaction> findByCreatedAtBetweenAndCategoryAndDivision(LocalDateTime start, LocalDateTime end, String category, String division, Pageable p);

}
