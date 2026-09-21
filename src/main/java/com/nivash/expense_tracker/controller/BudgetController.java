package com.nivash.expense_tracker.controller;

import com.nivash.expense_tracker.dto.BudgetRequest;
import com.nivash.expense_tracker.entity.Budget;
import com.nivash.expense_tracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<?> setBudget(Authentication authentication, @RequestBody BudgetRequest request) {
        Budget budget = budgetService.setBudget(authentication.getName(), request);
        return ResponseEntity.ok(budget);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getBudgetStatus(Authentication authentication,
                                              @RequestParam Integer month,
                                              @RequestParam Integer year) {
        Map<String, Object> status = budgetService.getBudgetStatus(authentication.getName(), month, year);
        return ResponseEntity.ok(status);
    }
}