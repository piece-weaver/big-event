package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.CategoryMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.Category;
import com.cxx.bigevent.service.ExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    private Map<Integer, String> categoryMap;

    private Map<Integer, String> getCategoryMap() {
        if (categoryMap == null) {
            categoryMap = new HashMap<>();
            List<Category> categories = categoryMapper.listAll();
            for (Category c : categories) {
                categoryMap.put(c.getId().intValue(), c.getCategoryName());
            }
        }
        return categoryMap;
    }

    @Override
    public byte[] exportArticlesToExcel(List<Article> articles) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("文章列表");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "标题", "内容", "分类", "状态", "创建时间", "更新时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            Map<Integer, String> categoryNameMap = getCategoryMap();

            int rowNum = 1;
            for (Article article : articles) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(article.getId());
                row.createCell(1).setCellValue(article.getTitle());
                row.createCell(2).setCellValue(article.getContent().length() > 100 ?
                        article.getContent().substring(0, 100) + "..." : article.getContent());
                row.createCell(3).setCellValue(categoryNameMap.getOrDefault(
                        article.getCategoryId() != null ? article.getCategoryId() : 0, "未知"));
                row.createCell(4).setCellValue(article.getState() != null && article.getState() == 1 ? "已发布" : "草稿");
                row.createCell(5).setCellValue(article.getCreateTime() != null ?
                        article.getCreateTime().toString() : "");
                row.createCell(6).setCellValue(article.getUpdateTime() != null ?
                        article.getUpdateTime().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    @Override
    public byte[] exportAllData() {
        List<Article> allArticles = articleMapper.list(null, null, null);
        return exportArticlesToExcel(allArticles);
    }
}
