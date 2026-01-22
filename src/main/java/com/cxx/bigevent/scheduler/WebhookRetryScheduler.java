package com.cxx.bigevent.scheduler;

import com.cxx.bigevent.service.WebhookRetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class WebhookRetryScheduler {

    @Autowired
    private WebhookRetryService webhookRetryService;

    @Scheduled(fixedRate = 60000)
    public void processRetryQueue() {
        webhookRetryService.processRetries();
    }
}
