package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.dto.AiSummaryResponse;
import com.cxx.bigevent.dto.AiTagsResponse;
import com.cxx.bigevent.mapper.ArticleAiTagsMapper;
import com.cxx.bigevent.mapper.ArticleMapper;
import com.cxx.bigevent.mapper.ArticleStatsMapper;
import com.cxx.bigevent.mapper.ArticleSummaryMapper;
import com.cxx.bigevent.pojo.Article;
import com.cxx.bigevent.pojo.ArticleAiTags;
import com.cxx.bigevent.pojo.ArticleSummary;
import com.cxx.bigevent.service.AiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AiServiceImpl implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceImpl.class);

    @Value("${ollama.host:http://localhost:11434}")
    private String ollamaHost;

    private String getOllamaHostUrl() {
        String host = ollamaHost != null ? ollamaHost.trim() : "";
        if (host.isEmpty() || host.equals(":11434") || host.equals("11434")) {
            return "http://localhost:11434";
        }
        if (!host.startsWith("http")) {
            return "http://" + host;
        }
        return host;
    }

    @Value("${ollama.model:deepseek-r1:8b}")
    private String defaultModel;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleSummaryMapper articleSummaryMapper;

    @Autowired
    private ArticleAiTagsMapper articleAiTagsMapper;

    @Autowired
    private ArticleStatsMapper articleStatsMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String AI_CACHE_PREFIX = "ai:cache:";
    private static final String AI_USAGE = "ai:usage:";
    private static final String AI_USAGE_PREFIX = "ai:usage:";

    @Override
    public AiSummaryResponse generateSummary(String content) {
        long startTime = System.currentTimeMillis();
        String model = getAvailableModel();

        String prompt = buildSummaryPrompt(content);
        String response = callOllama(prompt, model);

        AiSummaryResponse summaryResponse = parseSummaryResponse(response, content);
        summaryResponse.setModelUsed(model);
        summaryResponse.setProcessingTime(System.currentTimeMillis() - startTime);

        return summaryResponse;
    }

    @Override
    public AiSummaryResponse generateSummaryWithCache(Long articleId) {
        String cacheKey = AI_CACHE_PREFIX + "summary:" + articleId;
        String cached = stringRedisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            try {
                AiSummaryResponse cachedResponse = objectMapper.readValue(cached, AiSummaryResponse.class);
                log.info("从缓存获取文章摘要: articleId={}", articleId);
                return cachedResponse;
            } catch (JsonProcessingException e) {
                log.warn("缓存解析失败: {}", e.getMessage());
            }
        }

        Article article = articleMapper.findByIdOnly(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        AiSummaryResponse response = generateSummary(article.getContent());

        try {
            stringRedisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(response),
                    Duration.ofHours(24));
        } catch (JsonProcessingException e) {
            log.warn("缓存保存失败: {}", e.getMessage());
        }

        return response;
    }

    @Override
    public AiTagsResponse generateTags(String content) {
        long startTime = System.currentTimeMillis();
        String model = getAvailableModel();

        String prompt = buildTagsPrompt(content);
        String response = callOllama(prompt, model);

        AiTagsResponse tagsResponse = parseTagsResponse(response);
        tagsResponse.setModelUsed(model);
        tagsResponse.setProcessingTime(System.currentTimeMillis() - startTime);

        return tagsResponse;
    }

    @Override
    public AiTagsResponse generateTagsForArticle(Long articleId) {
        String cacheKey = AI_CACHE_PREFIX + "tags:" + articleId;
        String cached = stringRedisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            try {
                AiTagsResponse cachedResponse = objectMapper.readValue(cached, AiTagsResponse.class);
                log.info("从缓存获取文章标签: articleId={}", articleId);
                return cachedResponse;
            } catch (JsonProcessingException e) {
                log.warn("缓存解析失败: {}", e.getMessage());
            }
        }

        Article article = articleMapper.findByIdOnly(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        AiTagsResponse response = generateTags(article.getTitle() + "\n" + article.getContent());

        try {
            stringRedisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(response),
                    Duration.ofHours(24));
        } catch (JsonProcessingException e) {
            log.warn("缓存保存失败: {}", e.getMessage());
        }

        return response;
    }

    @Override
    public String improveContent(String content) {
        String model = getAvailableModel();
        String prompt = "请对以下内容进行润色和改进，提升文章质量和可读性。保持原文的核心意思，但优化表达方式、修正语法错误、使内容更流畅专业。\n\n原文：\n" + content;
        return callOllama(prompt, model);
    }

    @Override
    public String continueWriting(String content, String context) {
        String model = getAvailableModel();
        String prompt;
        if (context != null && !context.isEmpty()) {
            prompt = "请根据以下内容续写文章，保持风格一致，逻辑连贯。\n\n前文：\n" + content + "\n\n续写方向：" + context;
        } else {
            prompt = "请根据以下内容续写文章，保持风格一致，逻辑连贯。\n\n前文：\n" + content;
        }
        return callOllama(prompt, model);
    }

    @Override
    public String translate(String content, String targetLanguage) {
        String model = getAvailableModel();
        String prompt = "请将以下内容翻译成" + targetLanguage + "，保持原文的格式和风格。\n\n原文：\n" + content;
        return callOllama(prompt, model);
    }

    @Override
    public List<String> generateHeadlineSuggestions(String content) {
        String model = getAvailableModel();
        String prompt = "请为以下内容生成5个吸引人的标题建议，每个标题要简洁有力，能够准确概括文章主题。请用中文回复，格式为JSON数组。\n\n内容：\n" + content;
        String response = callOllama(prompt, model);
        return parseJsonArray(response);
    }

    @Override
    public String generateOutline(String content) {
        String model = getAvailableModel();
        String prompt = "请为以下内容生成文章大纲，包括标题和各级小节。格式要清晰，便于写作参考。\n\n内容：\n" + content;
        return callOllama(prompt, model);
    }

    @Override
    public Integer analyzeSeoScore(String title, String content) {
        String model = getAvailableModel();
        String prompt = "请分析以下文章的SEO优化程度，只返回一个0-100的分数，不需要解释。评分标准包括：标题吸引力、关键词使用、内容结构、可读性等。\n\n标题：" + title + "\n\n内容：" + content;
        String response = callOllama(prompt, model);
        return extractNumber(response);
    }

    @Override
    public SseEmitter streamGenerate(String prompt, String type) {
        SseEmitter emitter = new SseEmitter(300000L);
        String model = getAvailableModel();

        String fullPrompt = buildPromptByType(prompt, type);

        Request request = new Request.Builder()
                .url(getOllamaHostUrl() + "/api/generate")
                .post(RequestBody.create(
                        "{\"model\": \"" + model + "\", \"prompt\": \"" + escapeJson(fullPrompt) + "\", \"stream\": true}",
                        MediaType.parse("application/json")
                ))
                .build();

        EventSourceListener listener = new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                try {
                    Map<String, Object> response = objectMapper.readValue(data, Map.class);
                    String content = (String) response.get("response");
                    if (content != null && !content.isEmpty()) {
                        emitter.send(SseEmitter.event()
                                .name("message")
                                .data(content));
                    }
                } catch (Exception e) {
                    log.error("SSE解析错误: {}", e.getMessage());
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                emitter.complete();
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.error("SSE连接失败: {}", t.getMessage());
                emitter.completeWithError(t);
            }
        };

        EventSource eventSource = EventSources.createFactory(httpClient).newEventSource(request, listener);

        emitter.onTimeout(() -> {
            log.warn("SSE超时");
            eventSource.cancel();
            emitter.complete();
        });

        return emitter;
    }

    private String buildSummaryPrompt(String content) {
        int contentLength = content.length();
        int readingTime = (int) Math.ceil(contentLength / 500.0);

        return "请对以下文章进行深度分析，生成以下内容：\n" +
                "1. 简洁的摘要（100-200字）\n" +
                "2. 提取5-8个关键词\n" +
                "3. SEO评分（0-100）和改进建议\n" +
                "4. 推荐分类（从以下分类中选择：科技、生活、娱乐、体育、财经、教育、健康、旅游、美食、时尚、汽车、房产、人文、历史、艺术）\n" +
                "5. 预估阅读时间（" + readingTime + "分钟）\n\n" +
                "请以JSON格式回复，格式如下：\n" +
                "{\n" +
                "  \"summary\": \"摘要内容\",\n" +
                "  \"keywords\": [\"关键词1\", \"关键词2\", ...],\n" +
                "  \"seo_score\": 85,\n" +
                "  \"seo_suggestions\": [\"建议1\", \"建议2\", ...],\n" +
                "  \"suggested_category\": \"科技\",\n" +
                "  \"reading_time\": " + readingTime + "\n" +
                "}\n\n文章内容：\n" + content;
    }

    private String buildTagsPrompt(String content) {
        return "请分析以下内容，提取10个最相关的标签。标签应该简洁（2-4个字），能够准确描述文章主题。\n" +
                "请按相关性排序，返回JSON数组格式：[\"标签1\", \"标签2\", ...]\n\n内容：\n" + content;
    }

    private String buildPromptByType(String content, String type) {
        return switch (type) {
            case "summarize" -> buildSummaryPrompt(content);
            case "tags" -> buildTagsPrompt(content);
            case "improve" -> "请润色以下内容：\n" + content;
            case "continue" -> "请续写以下内容：\n" + content;
            case "translate" -> "请翻译以下内容为中文：\n" + content;
            case "outline" -> "请为以下内容生成文章大纲：\n" + content;
            default -> content;
        };
    }

    private String callOllama(String prompt, String model) {
        try {
            String jsonBody = String.format(
                    "{\"model\": \"%s\", \"prompt\": \"%s\", \"stream\": false}",
                    model, escapeJson(prompt)
            );

            RequestBody requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(getOllamaHostUrl() + "/api/generate")
                    .post(requestBody)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Ollama调用失败: " + response.code());
                }

                String responseBody = response.body() != null ? response.body().string() : null;
                
                if (responseBody == null) {
                    throw new RuntimeException("Ollama返回空响应");
                }
                
                Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                String rawResponse = (String) result.get("response");
                
                if (rawResponse != null && rawResponse.contains("<think>")) {
                    rawResponse = rawResponse.substring(rawResponse.lastIndexOf("</think>") + 9).trim();
                }
                
                trackUsage(model, rawResponse);

                return (String) result.get("response");
            }
        } catch (IOException e) {
            log.error("Ollama API调用错误: {}", e.getMessage());
            throw new RuntimeException("AI服务暂时不可用: " + e.getMessage());
        }
    }

    private AiSummaryResponse parseSummaryResponse(String response, String content) {
        AiSummaryResponse summaryResponse = new AiSummaryResponse();

        try {
            int start = response.indexOf("{");
            int end = response.lastIndexOf("}") + 1;
            if (start >= 0 && end > start) {
                String jsonStr = response.substring(start, end);
                Map<String, Object> result = objectMapper.readValue(jsonStr, Map.class);

                summaryResponse.setSummary((String) result.getOrDefault("summary", ""));
                summaryResponse.setKeywords((List<String>) result.getOrDefault("keywords", Collections.emptyList()));
                summaryResponse.setSeoSuggestions((List<String>) result.getOrDefault("seo_suggestions", Collections.emptyList()));
                summaryResponse.setSuggestedCategory((String) result.getOrDefault("suggested_category", ""));

                Object seoScore = result.get("seo_score");
                if (seoScore != null) {
                    summaryResponse.setSeoScore(((Number) seoScore).intValue());
                }

                Object readingTime = result.get("reading_time");
                if (readingTime != null) {
                    summaryResponse.setReadingTime(((Number) readingTime).intValue());
                } else {
                    summaryResponse.setReadingTime((int) Math.ceil(content.length() / 500.0));
                }
            } else {
                setDefaultSummary(summaryResponse, content);
            }
        } catch (JsonProcessingException e) {
            log.warn("摘要解析失败，使用默认解析: {}", e.getMessage());
            setDefaultSummary(summaryResponse, content);
        }

        return summaryResponse;
    }

    private void setDefaultSummary(AiSummaryResponse summaryResponse, String content) {
        summaryResponse.setSummary(content.substring(0, Math.min(200, content.length())) + "...");
        summaryResponse.setReadingTime((int) Math.ceil(content.length() / 500.0));
        summaryResponse.setKeywords(Collections.emptyList());
        summaryResponse.setSeoScore(60);
        summaryResponse.setSeoSuggestions(Collections.singletonList("建议添加更吸引人的标题"));
    }

    private AiTagsResponse parseTagsResponse(String response) {
        AiTagsResponse tagsResponse = new AiTagsResponse();
        List<AiTagsResponse.TagItem> tags = new ArrayList<>();

        try {
            int start = response.indexOf("[");
            int end = response.lastIndexOf("]") + 1;
            if (start >= 0 && end > start) {
                String jsonStr = response.substring(start, end);
                List<String> tagNames = objectMapper.readValue(jsonStr, List.class);

                double baseConfidence = 1.0;
                for (String tag : tagNames) {
                    AiTagsResponse.TagItem item = new AiTagsResponse.TagItem();
                    item.setName(tag);
                    item.setConfidence(baseConfidence);
                    tags.add(item);
                    baseConfidence -= 0.05;
                }
            }
        } catch (JsonProcessingException e) {
            log.warn("标签解析失败: {}", e.getMessage());
            parseTagsByRegex(response, tags);
        }

        if (tags.isEmpty()) {
            parseTagsByRegex(response, tags);
        }

        tagsResponse.setTags(tags);
        return tagsResponse;
    }

    private void parseTagsByRegex(String response, List<AiTagsResponse.TagItem> tags) {
        Pattern pattern = Pattern.compile("\"([\\u4e00-\\u9fa5a-zA-Z0-9]+)\"");
        Matcher matcher = pattern.matcher(response);
        double confidence = 0.9;
        while (matcher.find() && tags.size() < 10) {
            AiTagsResponse.TagItem item = new AiTagsResponse.TagItem();
            item.setName(matcher.group(1));
            item.setConfidence(confidence);
            tags.add(item);
            confidence -= 0.05;
        }
    }

    private List<String> parseJsonArray(String response) {
        List<String> result = new ArrayList<>();
        try {
            int start = response.indexOf("[");
            int end = response.lastIndexOf("]") + 1;
            if (start >= 0 && end > start) {
                String jsonStr = response.substring(start, end);
                result = objectMapper.readValue(jsonStr, List.class);
            }
        } catch (JsonProcessingException e) {
            log.warn("JSON数组解析失败: {}", e.getMessage());
        }
        return result;
    }

    private Integer extractNumber(String response) {
        Pattern pattern = Pattern.compile("\\b(\\d{1,3})\\b");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 60;
    }

    private String getAvailableModel() {
        try {
            Request request = new Request.Builder()
                    .url(getOllamaHostUrl() + "/api/tags")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String body = response.body() != null ? response.body().string() : null;
                    if (body != null && body.contains(defaultModel)) {
                        return defaultModel;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("检查可用模型失败: {}", e.getMessage());
        }
        return defaultModel;
    }

    private void trackUsage(String model, String response) {
        String usageKey = AI_USAGE + model + ":" + System.currentTimeMillis();
        stringRedisTemplate.opsForValue().set(usageKey, String.valueOf(response.length()),
                Duration.ofDays(30));
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
