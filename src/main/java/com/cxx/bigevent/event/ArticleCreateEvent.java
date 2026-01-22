package com.cxx.bigevent.event;

import com.cxx.bigevent.pojo.Article;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ArticleCreateEvent extends ApplicationEvent {
    private final Article article;

    public ArticleCreateEvent(Object source, Article article) {
        super(source);
        this.article = article;
    }
}
