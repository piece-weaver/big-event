package com.cxx.bigevent.service;

import com.cxx.bigevent.pojo.WebhookRetry;

import java.util.List;

public interface WebhookRetryService {

    void scheduleRetry(Long webhookId, String eventType, String payload, String error);

    void processRetries();

    List<WebhookRetry> getPendingRetries();
}
