package com.cxx.bigevent.service;

import com.cxx.bigevent.dto.WebhookRequest;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.Webhook;
import com.cxx.bigevent.pojo.WebhookLog;

public interface WebhookService {

    void createWebhook(WebhookRequest request);

    void updateWebhook(Long id, WebhookRequest request);

    void deleteWebhook(Long id);

    Webhook getWebhook(Long id);

    PageBean<Webhook> listWebhooks(Integer pageNum, Integer pageSize);

    void triggerEvent(String eventType, Object payload);

    PageBean<WebhookLog> getWebhookLogs(Long webhookId, Integer pageNum, Integer pageSize);

    void testWebhook(Long id);
}
