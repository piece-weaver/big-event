package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.mapper.WebhookLogMapper;
import com.cxx.bigevent.mapper.WebhookMapper;
import com.cxx.bigevent.mapper.WebhookRetryMapper;
import com.cxx.bigevent.pojo.Webhook;
import com.cxx.bigevent.pojo.WebhookLog;
import com.cxx.bigevent.pojo.WebhookRetry;
import com.cxx.bigevent.service.WebhookRetryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WebhookRetryServiceImpl implements WebhookRetryService {

    private static final Logger log = LoggerFactory.getLogger(WebhookRetryServiceImpl.class);
    private static final int MAX_ATTEMPTS = 3;
    private static final int RETRY_INTERVAL_MINUTES = 5;

    @Autowired
    private WebhookRetryMapper retryMapper;

    @Autowired
    private WebhookMapper webhookMapper;

    @Autowired
    private WebhookLogMapper webhookLogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void scheduleRetry(Long webhookId, String eventType, String payload, String error) {
        WebhookRetry retry = new WebhookRetry();
        retry.setWebhookId(webhookId);
        retry.setEventType(eventType);
        retry.setPayload(payload);
        retry.setAttemptCount(0);
        retry.setMaxAttempts(MAX_ATTEMPTS);
        retry.setLastError(error);
        retry.setNextRetryAt(LocalDateTime.now().plusMinutes(RETRY_INTERVAL_MINUTES));
        retry.setStatus(WebhookRetry.RetryStatus.PENDING);

        retryMapper.add(retry);
        log.info("Webhook重试任务已创建：webhookId={}，event={}", webhookId, eventType);
    }

    @Override
    public void processRetries() {
        List<WebhookRetry> reties = retryMapper.findPendingRetries(LocalDateTime.now());

        for (WebhookRetry retry : reties) {
            if (retry.getAttemptCount() >= retry.getMaxAttempts()) {
                retryMapper.updateRetry(retry.getId(), "达到最大重试次数",
                        null, WebhookRetry.RetryStatus.FAILED);
                log.warn("Webhook重试已放弃：retryId={}，webhookId={}",
                        retry.getId(), retry.getWebhookId());
                continue;
            }

            Webhook webhook = webhookMapper.findById(retry.getWebhookId());
            if (webhook == null || webhook.getIsActive() != 1) {
                retryMapper.markCompleted(retry.getId());
                continue;
            }

            retryMapper.updateRetry(retry.getId(), null, null, WebhookRetry.RetryStatus.RETRYING);

            try {
                executeRetry(webhook, retry);
            } catch (Exception e) {
                log.error("Webhook重试执行失败：retryId={}，error={}", retry.getId(), e.getMessage());
                retryMapper.updateRetry(retry.getId(), e.getMessage(),
                        LocalDateTime.now().plusMinutes(RETRY_INTERVAL_MINUTES * (retry.getAttemptCount() + 1)),
                        WebhookRetry.RetryStatus.PENDING);
            }
        }
    }

    private void executeRetry(Webhook webhook, WebhookRetry retry) throws Exception {
        long startTime = System.currentTimeMillis();
        WebhookLog logEntry = new WebhookLog();
        logEntry.setWebhookId(webhook.getId());
        logEntry.setEventType(retry.getEventType() + " (retry " + (retry.getAttemptCount() + 1) + ")");
        logEntry.setPayload(retry.getPayload());

        java.net.URL url = new java.net.URL(webhook.getUrl());
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("X-Webhook-Event", retry.getEventType());
        conn.setRequestProperty("X-Webhook-Signature", generateSignature(retry.getPayload(), webhook.getSecret()));
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(60000);

        try (java.io.OutputStream os = conn.getOutputStream()) {
            os.write(retry.getPayload().getBytes());
        }

        int status = conn.getResponseCode();
        logEntry.setResponseStatus(status);

        if (status >= 200 && status < 300) {
            logEntry.setStatus(WebhookLog.WebhookStatus.SUCCESS);
            retryMapper.markCompleted(retry.getId());
            webhookMapper.updateStats(webhook.getId(), 1, 0);
            log.info("Webhook重试成功：retryId={}，webhookId={}", retry.getId(), webhook.getId());
        } else {
            logEntry.setStatus(WebhookLog.WebhookStatus.FAILED);
            logEntry.setResponseBody(readResponse(conn.getErrorStream()));
            logEntry.setErrorMessage("HTTP " + status);

            if (retry.getAttemptCount() + 1 >= retry.getMaxAttempts()) {
                retryMapper.updateRetry(retry.getId(), "HTTP " + status,
                        null, WebhookRetry.RetryStatus.FAILED);
                log.warn("Webhook重试已用尽：retryId={}，webhookId={}", retry.getId(), webhook.getId());
            } else {
                retryMapper.updateRetry(retry.getId(), "HTTP " + status,
                        LocalDateTime.now().plusMinutes(RETRY_INTERVAL_MINUTES * (retry.getAttemptCount() + 2)),
                        WebhookRetry.RetryStatus.PENDING);
            }
            webhookMapper.updateStats(webhook.getId(), 0, 1);
        }

        logEntry.setDuration((int) (System.currentTimeMillis() - startTime));
        webhookLogMapper.add(logEntry);
    }

    private String generateSignature(String payload, String secret) {
        if (secret == null || secret.isEmpty()) return "";
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes());
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return "";
        }
    }

    private String readResponse(java.io.InputStream is) {
        if (is == null) return "";
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public List<WebhookRetry> getPendingRetries() {
        return retryMapper.findPendingRetries(LocalDateTime.now());
    }
}
