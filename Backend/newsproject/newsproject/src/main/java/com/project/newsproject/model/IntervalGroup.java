package com.project.newsproject.model;

import java.util.List;

public class IntervalGroup {
    private String interval;
    private int count;
    private List<NewsArticle> articles;

    public IntervalGroup(String interval, int count, List<NewsArticle> articles) {
        this.interval = interval;
        this.count = count;
        this.articles = articles;
    }

    public String getInterval() {
        return interval;
    }

    public int getCount() {
        return count;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }
}
