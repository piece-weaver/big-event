package com.cxx.bigevent.service;

import com.cxx.bigevent.dto.AiSummaryResponse;
import com.cxx.bigevent.dto.AiTagsResponse;
import com.cxx.bigevent.pojo.Article;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AiService {

    AiSummaryResponse generateSummary(String content);

    AiSummaryResponse generateSummaryWithCache(Long articleId);

    AiTagsResponse generateTags(String content);

    AiTagsResponse generateTagsForArticle(Long articleId);

    String improveContent(String content);

    String continueWriting(String content, String context);

    String translate(String content, String targetLanguage);

    List<String> generateHeadlineSuggestions(String content);

    String generateOutline(String content);

    Integer analyzeSeoScore(String title, String content);

    SseEmitter streamGenerate(String prompt, String type);
}
