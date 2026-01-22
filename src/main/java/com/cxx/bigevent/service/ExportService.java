package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.Article;

import java.util.List;

public interface ExportService {
    byte[] exportArticlesToExcel(List<Article> articles);
    byte[] exportAllData();
}
