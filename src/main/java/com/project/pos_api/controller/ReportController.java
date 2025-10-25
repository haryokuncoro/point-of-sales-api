package com.project.pos_api.controller;

import com.project.pos_api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales/daily")
    public Map<LocalDate, Double> daily(@RequestParam(defaultValue = "7") int days) {
        return reportService.getDailySales(days);
    }

    @GetMapping("/sales/chart")
    public ResponseEntity<byte[]> salesChart(@RequestParam(defaultValue = "7") int days) throws IOException {
        Map<LocalDate, Double> data = reportService.getDailySales(days);
        byte[] png = reportService.createSalesChart(data, 800, 400);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(png);
    }

    @GetMapping("/sales/pdf")
    public ResponseEntity<byte[]> salesPdf(@RequestParam(defaultValue = "7") int days) throws IOException {
        Map<LocalDate, Double> data = reportService.getDailySales(days);
        byte[] png = reportService.createSalesChart(data, 800, 400);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, png, "chart");
            PDType1Font font1 = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font font2 = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float scale = 0.5f;
                float imgW = pdImage.getWidth() * scale;
                float imgH = pdImage.getHeight() * scale;
                cs.drawImage(pdImage, 50, page.getMediaBox().getHeight() - imgH - 50, imgW, imgH);

                cs.beginText();
                cs.setFont(font1, 12);
                cs.newLineAtOffset(50, page.getMediaBox().getHeight() - imgH - 80);
                cs.showText("Daily Sales:"); // title
                cs.endText();

                float y = page.getMediaBox().getHeight() - imgH - 100;
                for (Map.Entry<LocalDate, Double> e : data.entrySet()) {
                    cs.beginText();
                    cs.setFont(font2, 11);
                    cs.newLineAtOffset(50, y);
                    cs.showText(e.getKey().toString() + " : " + String.format("%.2f", e.getValue()));
                    cs.endText();
                    y -= 15;
                }
            }
            doc.save(baos);
        }

        byte[] pdfBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "sales_report.pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
