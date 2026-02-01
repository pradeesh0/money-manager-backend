package com.MoneyManager.Controller;

import com.MoneyManager.Dto.TransactionDto;
import com.MoneyManager.Model.Transaction;
import com.MoneyManager.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public TransactionDto add(@RequestBody Transaction tx) {
        Transaction res = service.add(tx);
        return maptoDto(res);
    }

    @PutMapping("/{id}")
    public TransactionDto update(@PathVariable String id, @RequestBody Transaction tx) {
        Transaction res =  service.update(id, tx);
        return maptoDto(res);
    }

    @GetMapping
    public Map<String, Object> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Transaction> result = service.getAll(
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        return buildPageResponse(result);
    }

    @GetMapping("/between")
    public Map<String, Object> between(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String division,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Transaction> result = service.getBetweenFiltered(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                category,
                division,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        return buildPageResponse(result);
    }


    @GetMapping("/summary/category")
    public List<Map<String,Object>> categorySummary(
            @RequestParam String start,
            @RequestParam String end) {

        return service.getCategorySummary(start, end);
    }

    @GetMapping("/filter")
    public Map<String, Object> filter(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String division,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Transaction> result = service.filter(
                category == null || category.isBlank() ? null : category,
                division == null || division.isBlank() ? null : division,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        return buildPageResponse(result);
    }

    private Map<String, Object> buildPageResponse(Page<Transaction> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent().stream().map(this::maptoDto).toList());
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("pageSize", page.getSize());
        return response;
    }

    public TransactionDto maptoDto(Transaction transaction)
    {
        TransactionDto temp=new TransactionDto();
        temp.setId(transaction.getId());
        temp.setType(transaction.getType());
        temp.setAmount(transaction.getAmount());
        temp.setDescription(transaction.getDescription());
        temp.setCategory(transaction.getCategory());
        temp.setDivision(transaction.getDivision());
        temp.setCreatedAt(transaction.getCreatedAt());
        temp.setUpdatedAt(transaction.getUpdatedAt());

        return temp;
    }
}

