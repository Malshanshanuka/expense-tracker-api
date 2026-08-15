package com.malshan.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
    private String categoryName;
}