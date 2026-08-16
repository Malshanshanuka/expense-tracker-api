package com.malshan.expense_tracker.controller;

import com.malshan.expense_tracker.dto.MonthlySummaryResponse;
import com.malshan.expense_tracker.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month
    ) {
        MonthlySummaryResponse response = reportService.getMonthlySummary(year, month);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/monthly/pdf")
    public ResponseEntity<InputStreamResource> downloadMonthlyReportPdf(
            @RequestParam int year,
            @RequestParam int month
    ) {
        ByteArrayInputStream pdfStream = reportService.generateMonthlyReportPdf(year, month);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expense-report-" + year + "-" + month + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}