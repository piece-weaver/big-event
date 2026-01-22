package com.cxx.bigevent.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ArticleDeleteEvent extends ApplicationEvent {
    private final Long articleId;
    private final String articleTitle;

    public ArticleDeleteEvent(Object source, Long articleId, String articleTitle) {
        super(source);
        this.articleId = articleId;
        this.articleTitle = articleTitle;
    }
}
