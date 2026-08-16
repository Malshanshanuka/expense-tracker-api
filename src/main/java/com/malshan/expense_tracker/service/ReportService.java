package com.malshan.expense_tracker.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.malshan.expense_tracker.dto.CategorySummary;
import com.malshan.expense_tracker.dto.MonthlySummaryResponse;
import com.malshan.expense_tracker.entity.Expense;
import com.malshan.expense_tracker.entity.User;
import com.malshan.expense_tracker.exception.ResourceNotFoundException;
import com.malshan.expense_tracker.repository.ExpenseRepository;
import com.malshan.expense_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ReportService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public MonthlySummaryResponse getMonthlySummary(int year, int month) {
        User currentUser = getCurrentUser();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(
                currentUser.getId(), startDate, endDate
        );


        BigDecimal totalSpent = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Map<String, BigDecimal> groupedByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        List<CategorySummary> breakdown = groupedByCategory.entrySet().stream()
                .map(entry -> new CategorySummary(entry.getKey(), entry.getValue()))
                .toList();

        return new MonthlySummaryResponse(totalSpent, breakdown);
    }
    public ByteArrayInputStream generateMonthlyReportPdf(int year, int month) {
        MonthlySummaryResponse summary = getMonthlySummary(year, month);
        User currentUser = getCurrentUser();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);


            document.add(new Paragraph("Expense Report")
                    .setFontSize(20)
                    .setBold()
                    .setUnderline())
                    .setFontSize(20)
                    .setBold()
                    .setUnderline(1f, -2f);

            document.add(new Paragraph("User: " + currentUser.getFullName()));
            document.add(new Paragraph("Period: " + month + "/" + year));
            document.add(new Paragraph("Total Spent: Rs. " + summary.getTotalSpent()));
            document.add(new Paragraph(" ")); // Space ekak


            Table table = new Table(2);
            table.addHeaderCell("Category");
            table.addHeaderCell("Amount");

            for (CategorySummary category : summary.getCategoryBreakdown()) {
                table.addCell(category.getCategoryName());
                table.addCell("Rs. " + category.getTotalAmount());
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}