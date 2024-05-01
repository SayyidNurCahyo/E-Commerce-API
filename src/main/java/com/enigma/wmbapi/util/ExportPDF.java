package com.enigma.wmbapi.util;

import com.enigma.wmbapi.dto.response.GetTransactionResponse;
import com.enigma.wmbapi.dto.response.TransactionDetailResponse;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.security.core.parameters.P;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ExportPDF {
    public static ByteArrayInputStream transactionsReport(List<GetTransactionResponse> transactionResponses) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(90);
        table.setWidths(new int[] { 4, 4, 4, 4, 4, 4, 4 });

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Date", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Customer", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Table", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Menu", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Quantity", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Amount", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Status", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        for (GetTransactionResponse transactionResponse:transactionResponses) {
            int size = transactionResponse.getTransactionDetails().size();
            for (int i=0; i<size;i++) {
                PdfPCell cellDate = new PdfPCell(new Phrase(transactionResponse.getTransactionDate()));
                cellDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellDate);

                PdfPCell cellCustomer = new PdfPCell(new Phrase(transactionResponse.getCustomerName()));
                cellCustomer.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellCustomer.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellCustomer);

                PdfPCell cellTable = new PdfPCell(new Phrase(transactionResponse.getTable()));
                cellTable.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellTable);

                PdfPCell cellMenu = new PdfPCell(new Phrase(transactionResponse.getTransactionDetails().get(i).getMenu()));
                cellMenu.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellMenu.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellMenu);

                PdfPCell cellQuantity = new PdfPCell(new Phrase(String.valueOf(transactionResponse.getTransactionDetails().get(i).getMenuQuantity())));
                cellQuantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellQuantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellQuantity);

                PdfPCell cellAmount = new PdfPCell(new Phrase(String.valueOf(transactionResponse.getTransactionDetails().get(i).getMenuQuantity()
                *transactionResponse.getTransactionDetails().get(i).getMenuPrice())));
                cellAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellAmount);

                PdfPCell cellStatus = new PdfPCell(new Phrase(transactionResponse.getPaymentResponse().getTransactionStatus()));
                cellStatus.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellStatus);
            }

        }
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(table);
        document.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
