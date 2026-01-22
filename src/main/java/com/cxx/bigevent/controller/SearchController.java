package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.SearchRequest;
import com.cxx.bigevent.dto.SearchResponse;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Tag(name = "搜索服务", description = "文章搜索和推荐相关接口")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    @Operation(summary = "搜索文章", description = "根据关键词搜索文章，支持分页和筛选")
    public Result<SearchResponse> search(SearchRequest request) {
        SearchResponse response = searchService.search(request);
        return Result.success(response);
    }

    @GetMapping("/articles")
    @Operation(summary = "搜索文章", description = "根据关键词搜索文章，支持分页和筛选")
    public Result<SearchResponse> searchArticles(SearchRequest request) {
        SearchResponse response = searchService.search(request);
        return Result.success(response);
    }

    @GetMapping("/suggest")
    @Operation(summary = "搜索建议", description = "根据前缀获取搜索建议词")
    public Result<List<String>> suggest(@RequestParam String prefix) {
        List<String> suggestions = searchService.suggest(prefix);
        return Result.success(suggestions);
    }

    @GetMapping("/suggestions")
    @Operation(summary = "搜索建议", description = "根据前缀获取搜索建议词")
    public Result<List<String>> suggestions(@RequestParam String prefix) {
        List<String> suggestions = searchService.suggest(prefix);
        return Result.success(suggestions);
    }

    @GetMapping("/hot")
    @Operation(summary = "热门搜索", description = "获取热门搜索关键词")
    public Result<List<String>> hotKeywords(@RequestParam(defaultValue = "10") int limit) {
        if (searchService instanceof com.cxx.bigevent.service.impl.SearchServiceImpl) {
            List<String> hotKeywords = ((com.cxx.bigevent.service.impl.SearchServiceImpl) searchService).getHotKeywords(limit);
            return Result.success(hotKeywords);
        }
        return Result.success(List.of());
    }

    @PostMapping("/sync")
    @Operation(summary = "同步索引", description = "将文章数据同步到搜索引擎索引")
    public Result<Void> sync() {
        searchService.syncAllArticles();
        return Result.success();
    }
}
