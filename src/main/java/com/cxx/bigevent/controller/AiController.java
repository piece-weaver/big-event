package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.AiSummaryResponse;
import com.cxx.bigevent.dto.AiTagsResponse;
import com.cxx.bigevent.dto.SeoScoreRequest;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI服务", description = "基于 Ollama 的 AI 内容生成和分析接口")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/summarize")
    @Operation(summary = "生成摘要", description = "对给定内容生成简洁的摘要")
    public Result<AiSummaryResponse> generateSummary(@RequestBody String content) {
        AiSummaryResponse response = aiService.generateSummary(content);
        return Result.success(response);
    }

    @PostMapping("/summarize/{articleId}")
    @Operation(summary = "生成文章摘要", description = "根据文章ID生成文章摘要（带缓存）")
    public Result<AiSummaryResponse> generateSummaryForArticle(@PathVariable Long articleId) {
        AiSummaryResponse response = aiService.generateSummaryWithCache(articleId);
        return Result.success(response);
    }

    @PostMapping("/tags")
    @Operation(summary = "生成标签", description = "根据内容自动生成相关标签")
    public Result<AiTagsResponse> generateTags(@RequestBody String content) {
        AiTagsResponse response = aiService.generateTags(content);
        return Result.success(response);
    }

    @PostMapping("/tags/{articleId}")
    @Operation(summary = "生成文章标签", description = "根据文章ID生成文章标签（带缓存）")
    public Result<AiTagsResponse> generateTagsForArticle(@PathVariable Long articleId) {
        AiTagsResponse response = aiService.generateTagsForArticle(articleId);
        return Result.success(response);
    }

    @PostMapping("/improve")
    @Operation(summary = "优化内容", description = "使用AI优化和改写内容")
    public Result<String> improveContent(@RequestBody String content) {
        String improved = aiService.improveContent(content);
        return Result.success(improved);
    }

    @PostMapping("/continue")
    @Operation(summary = "续写内容", description = "根据给定内容进行AI续写")
    public Result<String> continueWriting(@RequestBody String content) {
        String continued = aiService.continueWriting(content, null);
        return Result.success(continued);
    }

    @PostMapping("/continue/{context}")
    @Operation(summary = "续写内容(带上下文)", description = "根据给定内容和上下文进行AI续写")
    public Result<String> continueWritingWithContext(@RequestBody String content, @PathVariable String context) {
        String continued = aiService.continueWriting(content, context);
        return Result.success(continued);
    }

    @PostMapping("/translate")
    @Operation(summary = "内容翻译", description = "将内容翻译成指定语言")
    public Result<String> translate(@RequestBody String content,
                                    @RequestParam(defaultValue = "中文") String targetLanguage) {
        String translated = aiService.translate(content, targetLanguage);
        return Result.success(translated);
    }

    @PostMapping("/headlines")
    @Operation(summary = "生成标题", description = "根据内容生成吸引人的标题建议")
    public Result<List<String>> generateHeadlines(@RequestBody String content) {
        List<String> headlines = aiService.generateHeadlineSuggestions(content);
        return Result.success(headlines);
    }

    @PostMapping("/outline")
    @Operation(summary = "生成大纲", description = "根据内容生成文章大纲")
    public Result<String> generateOutline(@RequestBody String content) {
        String outline = aiService.generateOutline(content);
        return Result.success(outline);
    }

    @PostMapping("/seo-score")
    @Operation(summary = "SEO评分", description = "分析内容的SEO优化程度并给出评分")
    public Result<Integer> analyzeSeoScore(@RequestBody SeoScoreRequest request) {
        Integer score = aiService.analyzeSeoScore(request.getTitle(), request.getContent());
        return Result.success(score);
    }

    @GetMapping("/stream")
    @Operation(summary = "流式生成", description = "使用SSE流式输出AI生成结果")
    public SseEmitter streamGenerate(@RequestParam String prompt, @RequestParam String type) {
        return aiService.streamGenerate(prompt, type);
    }
}
