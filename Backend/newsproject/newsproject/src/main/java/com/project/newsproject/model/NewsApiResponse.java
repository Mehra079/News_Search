package com.project.newsproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NewsApiResponse {
    private List<NewsArticle> articles;

    @JsonProperty("articles")
    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
    }
}
