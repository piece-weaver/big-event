package com.cxx.bigevent.service.impl;

import com.cxx.bigevent.dto.WebhookRequest;
import com.cxx.bigevent.exception.BusinessException;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.mapper.WebhookLogMapper;
import com.cxx.bigevent.mapper.WebhookMapper;
import com.cxx.bigevent.pojo.PageBean;
import com.cxx.bigevent.pojo.Webhook;
import com.cxx.bigevent.pojo.WebhookLog;
import com.cxx.bigevent.service.WebhookService;
import com.cxx.bigevent.util.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class WebhookServiceImpl implements WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookServiceImpl.class);

    @Autowired
    private WebhookMapper webhookMapper;

    @Autowired
    private WebhookLogMapper webhookLogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWebhook(WebhookRequest request) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Webhook webhook = new Webhook();
        webhook.setName(request.getName());
        webhook.setUrl(request.getUrl());
        webhook.setSecret(request.getSecret());
        try {
            webhook.setEvents(objectMapper.writeValueAsString(request.getEvents()));
        } catch (Exception e) {
            throw new BusinessException("事件格式转换失败");
        }
        webhook.setIsActive(request.getIsActive());
        webhook.setCreatedBy(userId.longValue());

        webhookMapper.add(webhook);
        log.info("Webhook创建成功：{}，ID：{}", webhook.getName(), webhook.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWebhook(Long id, WebhookRequest request) {
        Webhook existing = webhookMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.WEBHOOK_NOT_FOUND);
        }

        existing.setName(request.getName());
        existing.setUrl(request.getUrl());
        existing.setSecret(request.getSecret());
        try {
            existing.setEvents(objectMapper.writeValueAsString(request.getEvents()));
        } catch (Exception e) {
            throw new BusinessException("事件格式转换失败");
        }
        existing.setIsActive(request.getIsActive());

        webhookMapper.update(existing);
        log.info("Webhook更新成功：{}，ID：{}", existing.getName(), id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWebhook(Long id) {
        Webhook webhook = webhookMapper.findById(id);
        if (webhook == null) {
            throw new BusinessException(ErrorCode.WEBHOOK_NOT_FOUND);
        }

        webhookMapper.delete(id);
        webhookLogMapper.deleteByWebhookId(id);
        log.info("Webhook删除成功：{}，ID：{}", webhook.getName(), id);
    }

    @Override
    public Webhook getWebhook(Long id) {
        Webhook webhook = webhookMapper.findById(id);
        if (webhook == null) {
            throw new BusinessException(ErrorCode.WEBHOOK_NOT_FOUND);
        }
        return webhook;
    }

    @Override
    public PageBean<Webhook> listWebhooks(Integer pageNum, Integer pageSize) {
        PageBean<Webhook> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Webhook> webhooks = webhookMapper.list();
        Page<Webhook> p = (Page<Webhook>) webhooks;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public void triggerEvent(String eventType, Object payload) {
        List<Webhook> webhooks = webhookMapper.findActiveWebhooks();

        for (Webhook webhook : webhooks) {
            if (shouldTrigger(webhook.getEvents(), eventType)) {
                executeWebhook(webhook, eventType, payload);
            }
        }
    }

    private boolean shouldTrigger(String eventsJson, String eventType) {
        if (eventsJson == null || eventsJson.isEmpty()) {
            return false;
        }
        try {
            List<String> eventList = objectMapper.readValue(eventsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            return eventList.contains(eventType);
        } catch (Exception e) {
            WebhookServiceImpl.log.warn("解析Webhook事件失败：{}", eventsJson);
            return false;
        }
    }

    private void executeWebhook(Webhook webhook, String eventType, Object payload) {
        long startTime = System.currentTimeMillis();
        WebhookLog log = new WebhookLog();
        log.setWebhookId(webhook.getId());
        log.setEventType(eventType);

        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            log.setPayload(payloadJson);

            java.net.URL url = new java.net.URL(webhook.getUrl());
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Webhook-Event", eventType);
            conn.setRequestProperty("X-Webhook-Signature", generateSignature(payloadJson, webhook.getSecret()));
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);

            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(payloadJson.getBytes());
            }

            int status = conn.getResponseCode();
            log.setResponseStatus(status);

            if (status == 200 || status == 201 || status == 204) {
                log.setStatus(WebhookLog.WebhookStatus.SUCCESS);
                webhookMapper.updateStats(webhook.getId(), 1, 0);
            } else {
                log.setStatus(WebhookLog.WebhookStatus.FAILED);
                String responseBody = readResponse(conn.getErrorStream());
                log.setResponseBody(responseBody);
                log.setErrorMessage("HTTP " + status);
                webhookMapper.updateStats(webhook.getId(), 0, 1);
            }
        } catch (Exception e) {
            log.setStatus(WebhookLog.WebhookStatus.FAILED);
            log.setErrorMessage(e.getMessage());
            webhookMapper.updateStats(webhook.getId(), 0, 1);
            WebhookServiceImpl.log.error("Webhook执行失败：{}，错误：{}", webhook.getName(), e.getMessage());
        } finally {
            log.setDuration((int) (System.currentTimeMillis() - startTime));
            webhookLogMapper.add(log);
        }
    }

    private String generateSignature(String payload, String secret) {
        if (secret == null || secret.isEmpty()) {
            return "";
        }
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes());
            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
            return encoder.encodeToString(hash);
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
    public PageBean<WebhookLog> getWebhookLogs(Long webhookId, Integer pageNum, Integer pageSize) {
        PageBean<WebhookLog> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<WebhookLog> logs = webhookLogMapper.findByWebhookId(webhookId);
        Page<WebhookLog> p = (Page<WebhookLog>) logs;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public void testWebhook(Long id) {
        Webhook webhook = webhookMapper.findById(id);
        if (webhook == null) {
            throw new BusinessException(ErrorCode.WEBHOOK_NOT_FOUND);
        }

        String testPayload = "{\"event\":\"test\",\"timestamp\":\"" +
                java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) + "\"}";

        Object payload = new Object() {
            public String event = "test";
            public String timestamp = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        };

        executeWebhook(webhook, "webhook.test", payload);
    }
}
