package com.nivash.expense_tracker.controller;

import com.nivash.expense_tracker.dto.ExpenseRequest;
import com.nivash.expense_tracker.entity.Expense;
import com.nivash.expense_tracker.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<?> addExpense(Authentication authentication, @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.addExpense(authentication.getName(), request);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<?> getExpenses(Authentication authentication,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate) {
        List<Expense> expenses;
        if (startDate != null && endDate != null) {
            expenses = expenseService.getExpensesByDateRange(
                    authentication.getName(),
                    java.time.LocalDate.parse(startDate),
                    java.time.LocalDate.parse(endDate)
            );
        } else {
            expenses = expenseService.getExpenses(authentication.getName());
        }
        return ResponseEntity.ok(expenses);
    }
    @GetMapping("/summary")
    public ResponseEntity<?> getCategorySummary(Authentication authentication) {
        List<Object[]> results = expenseService.getCategoryWiseTotal(authentication.getName());
        return ResponseEntity.ok(results);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(Authentication authentication, @PathVariable Long id, @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.updateExpense(authentication.getName(), id, request);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(Authentication authentication, @PathVariable Long id) {
        expenseService.deleteExpense(authentication.getName(), id);
        return ResponseEntity.ok("Expense deleted successfully");
    }
}