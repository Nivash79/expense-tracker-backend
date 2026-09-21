package com.nivash.expense_tracker.service;

import com.nivash.expense_tracker.dto.BudgetRequest;
import com.nivash.expense_tracker.entity.Budget;
import com.nivash.expense_tracker.entity.Expense;
import com.nivash.expense_tracker.entity.User;
import com.nivash.expense_tracker.repository.BudgetRepository;
import com.nivash.expense_tracker.repository.ExpenseRepository;
import com.nivash.expense_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Budget setBudget(String userEmail, BudgetRequest request) {
        User user = getUserByEmail(userEmail);

        Budget budget = budgetRepository.findByUserIdAndMonthAndYear(user.getId(), request.getMonth(), request.getYear())
                .orElse(new Budget());

        budget.setUser(user);
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setLimitAmount(request.getLimitAmount());

        return budgetRepository.save(budget);
    }

    public Map<String, Object> getBudgetStatus(String userEmail, Integer month, Integer year) {
        User user = getUserByEmail(userEmail);

        Budget budget = budgetRepository.findByUserIdAndMonthAndYear(user.getId(), month, year)
                .orElseThrow(() -> new RuntimeException("No budget set for this month"));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        BigDecimal totalSpent = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = budget.getLimitAmount().subtract(totalSpent);
        boolean exceeded = totalSpent.compareTo(budget.getLimitAmount()) > 0;

        Map<String, Object> status = new HashMap<>();
        status.put("month", month);
        status.put("year", year);
        status.put("budgetLimit", budget.getLimitAmount());
        status.put("totalSpent", totalSpent);
        status.put("remaining", remaining);
        status.put("exceeded", exceeded);

        return status;
    }
}