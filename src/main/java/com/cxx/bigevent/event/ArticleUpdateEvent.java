package com.cxx.bigevent.event;

import com.cxx.bigevent.pojo.Article;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ArticleUpdateEvent extends ApplicationEvent {
    private final Article article;
    private final Long articleId;

    public ArticleUpdateEvent(Object source, Article article, Long articleId) {
        super(source);
        this.article = article;
        this.articleId = articleId;
    }
}
