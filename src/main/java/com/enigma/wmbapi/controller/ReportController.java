package com.enigma.wmbapi.controller;

import com.enigma.wmbapi.constant.APIUrl;
import com.enigma.wmbapi.dto.response.GetTransactionResponse;
import com.enigma.wmbapi.service.TransactionService;
import com.enigma.wmbapi.util.ExportPDF;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.REPORT_API)
public class ReportController {
    private final TransactionService transactionService;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(path = "/exportpdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> transactionReports() throws Exception{
        List<GetTransactionResponse> transactions = transactionService.getTransaction();
        ByteArrayInputStream inputStream = ExportPDF.transactionsReport(transactions);
        String headers = String.format("attachment;filename=%s",System.currentTimeMillis()+"_transactions.pdf");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, headers).body(new InputStreamResource(inputStream));
    }
}
