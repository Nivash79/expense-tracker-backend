package com.nivash.expense_tracker;

import com.nivash.expense_tracker.entity.Category;
import com.nivash.expense_tracker.entity.Expense;
import com.nivash.expense_tracker.entity.User;
import com.nivash.expense_tracker.repository.CategoryRepository;
import com.nivash.expense_tracker.repository.ExpenseRepository;
import com.nivash.expense_tracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class ExpenseTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerApplication.class, args);
    }
}