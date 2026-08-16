package com.malshan.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryResponse {
    private BigDecimal totalSpent;
    private List<CategorySummary> categoryBreakdown;
}