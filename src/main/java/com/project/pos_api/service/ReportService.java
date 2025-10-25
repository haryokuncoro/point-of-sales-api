package com.project.pos_api.service;

import com.project.pos_api.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;

    public Map<LocalDate, Double> getDailySales(int daysBack) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        Map<LocalDate, Double> map = new LinkedHashMap<>();
        for (int i = daysBack - 1; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            map.put(d, 0.0);
        }

        saleRepository.findAll().forEach(sale -> {
            LocalDate d = sale.getDate().toLocalDate();
            if (map.containsKey(d)) {
                map.put(d, map.get(d) + sale.getTotal().doubleValue());
            }
        });

        return map;
    }

    public byte[] createSalesChart(Map<LocalDate, Double> dailySales, int width, int height) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dailySales.forEach((date, total) -> dataset.addValue(total, "Sales", date.toString()));

        JFreeChart chart = ChartFactory.createLineChart(
                "Daily Sales", "Date", "Total", dataset, PlotOrientation.VERTICAL, false, true, false);

        BufferedImage img = chart.createBufferedImage(width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeBufferedImageAsPNG(baos, img);
        return baos.toByteArray();
    }
}
