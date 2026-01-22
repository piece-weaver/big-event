package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.WebhookRequest;
import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.pojo.Webhook;
import com.cxx.bigevent.pojo.WebhookLog;
import com.cxx.bigevent.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@Validated
@Tag(name = "Webhook管理", description = "Webhook 配置和调用记录管理接口")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private WebhookService webhookService;

    @PostMapping
    @Operation(summary = "创建Webhook", description = "创建一个新的 Webhook 配置")
    public Result<Void> create(@RequestBody @Valid WebhookRequest request) {
        webhookService.createWebhook(request);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "Webhook列表", description = "获取所有 Webhook 配置列表")
    public Result<PageBean<Webhook>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<Webhook> pb = webhookService.listWebhooks(pageNum, pageSize);
        return Result.success(pb);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取Webhook", description = "根据ID获取 Webhook 配置详情")
    public Result<Webhook> get(@PathVariable Long id) {
        Webhook webhook = webhookService.getWebhook(id);
        return Result.success(webhook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新Webhook", description = "更新指定的 Webhook 配置")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid WebhookRequest request) {
        webhookService.updateWebhook(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除Webhook", description = "删除指定的 Webhook 配置")
    public Result<Void> delete(@PathVariable Long id) {
        webhookService.deleteWebhook(id);
        return Result.success();
    }

    @PostMapping("/{id}/test")
    @Operation(summary = "测试Webhook", description = "测试指定的 Webhook 是否可用")
    public Result<Void> test(@PathVariable Long id) {
        webhookService.testWebhook(id);
        return Result.success();
    }

    @GetMapping("/{id}/logs")
    @Operation(summary = "获取调用日志", description = "获取指定 Webhook 的调用记录")
    public Result<PageBean<WebhookLog>> logs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean<WebhookLog> pb = webhookService.getWebhookLogs(id, pageNum, pageSize);
        return Result.success(pb);
    }
}
