package com.nivash.expense_tracker.dto;

import java.math.BigDecimal;

public class BudgetRequest {
    private Integer month;
    private Integer year;
    private BigDecimal limitAmount;

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public BigDecimal getLimitAmount() { return limitAmount; }
    public void setLimitAmount(BigDecimal limitAmount) { this.limitAmount = limitAmount; }
}