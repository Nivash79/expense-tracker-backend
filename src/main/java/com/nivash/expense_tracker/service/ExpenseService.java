package com.nivash.expense_tracker.service;

import com.nivash.expense_tracker.dto.ExpenseRequest;
import com.nivash.expense_tracker.entity.Category;
import com.nivash.expense_tracker.entity.Expense;
import com.nivash.expense_tracker.entity.User;
import com.nivash.expense_tracker.repository.CategoryRepository;
import com.nivash.expense_tracker.repository.ExpenseRepository;
import com.nivash.expense_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Expense addExpense(String userEmail, ExpenseRequest request) {
        User user = getUserByEmail(userEmail);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setNotes(request.getNotes());
        expense.setUser(user);
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public List<Expense> getExpenses(String userEmail) {
        User user = getUserByEmail(userEmail);
        return expenseRepository.findByUserId(user.getId());
    }
    public List<Expense> getExpensesByDateRange(String userEmail, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        User user = getUserByEmail(userEmail);
        return expenseRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
    }

    public Expense updateExpense(String userEmail, Long expenseId, ExpenseRequest request) {
        User user = getUserByEmail(userEmail);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to update this expense");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setNotes(request.getNotes());
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public void deleteExpense(String userEmail, Long expenseId) {
        User user = getUserByEmail(userEmail);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }
    public List<Object[]> getCategoryWiseTotal(String userEmail) {
        User user = getUserByEmail(userEmail);
        return expenseRepository.getCategoryWiseTotal(user.getId());
    }
}