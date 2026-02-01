package com.MoneyManager.Service;

import com.MoneyManager.Model.Transaction;
import com.MoneyManager.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    public Transaction add(Transaction tx) {
        if (tx.getType() == null)
            throw new RuntimeException("Transaction type is required");

        tx.setCreatedAt(LocalDateTime.now());
        return repo.save(tx);
    }

    public Transaction update(String id, Transaction updated) {
        Transaction existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (Duration.between(existing.getCreatedAt(), LocalDateTime.now()).toHours() > 12) {
            throw new RuntimeException("Editing time expired");
        }

        existing.setAmount(updated.getAmount());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setDivision(updated.getDivision());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    public Page<Transaction> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }


    public List<Map<String,Object>> getCategorySummary(String start, String end) {
        return repo.categorySummary(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        );
    }

    public Page<Transaction> filter(String category, String division, Pageable pageable) {
        if (category == null && division == null)
            return repo.findAll(pageable);

        if (category == null)
            return repo.findByDivision(division, pageable);

        if (division == null)
            return repo.findByCategory(category, pageable);

        return repo.findByCategoryAndDivision(category, division, pageable);
    }

    public Page<Transaction> getBetweenFiltered(LocalDateTime start,
                                                LocalDateTime end,
                                                String category,
                                                String division,
                                                Pageable pageable) {

        if (category == null && division == null)
            return repo.findByCreatedAtBetween(start, end, pageable);

        if (category == null)
            return repo.findByCreatedAtBetweenAndDivision(start, end, division, pageable);

        if (division == null)
            return repo.findByCreatedAtBetweenAndCategory(start, end, category, pageable);

        return repo.findByCreatedAtBetweenAndCategoryAndDivision(start, end, category, division, pageable);
    }

}

