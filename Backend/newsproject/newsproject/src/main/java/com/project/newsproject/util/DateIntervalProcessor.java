package com.project.newsproject.util;

import com.project.newsproject.model.NewsArticle;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DateIntervalProcessor {

    public List<NewsArticle> filterArticlesByInterval(List<NewsArticle> articles, int interval) {
        LocalDateTime now = LocalDateTime.now();
        return articles.stream()
                .filter(article -> {
                    LocalDateTime publishedDate = LocalDateTime.parse(article.getPublishedAt(),
                            DateTimeFormatter.ISO_DATE_TIME);
                    return publishedDate.isAfter(now.minusHours(interval)); // Adjust according to the interval type
                })
                .collect(Collectors.toList());
    }
}
