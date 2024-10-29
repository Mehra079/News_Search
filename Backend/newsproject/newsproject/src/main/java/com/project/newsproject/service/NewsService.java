package com.project.newsproject.service;

import com.project.newsproject.model.NewsApiResponse;
import com.project.newsproject.model.NewsArticle;
import com.project.newsproject.model.IntervalGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class NewsService {

    private final RestTemplate restTemplate;

    @Value("${newsapi.Key}")
    private String apiKey;

    // Use in-memory cache for simplicity
    public Map<String, List<NewsArticle>> cache = new HashMap<>();

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getNews(String keyword, int intervalValue, String intervalUnit, boolean offlineMode) {
        List<NewsArticle> articles;

        if (offlineMode) {
            // Return cached articles if offline
            articles = cache.getOrDefault(keyword, new ArrayList<>());
        } else {
            // Fetch articles from News API
            String url = "https://newsapi.org/v2/everything?q=" + keyword + "&apiKey=" + apiKey;
            NewsApiResponse response = restTemplate.getForObject(url, NewsApiResponse.class);
            articles = response != null ? response.getArticles() : new ArrayList<>();

            // Cache the news articles
            cache.put(keyword, articles);
        }

        // Sort articles by publishedAt in descending order
        articles.sort((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt()));

        // Group articles by interval
        Map<String, List<NewsArticle>> groupedArticles = groupByInterval(articles, intervalValue, intervalUnit);

        // Prepare the final response
        Map<String, Object> finalResponse = new HashMap<>();
        List<IntervalGroup> intervalGroups = new ArrayList<>();

        for (Map.Entry<String, List<NewsArticle>> entry : groupedArticles.entrySet()) {
            intervalGroups.add(new IntervalGroup(entry.getKey(), entry.getValue().size(), entry.getValue()));
        }

        // Sort intervals from latest to oldest
        intervalGroups.sort((a, b) -> b.getInterval().compareTo(a.getInterval()));

        finalResponse.put("intervalGroups", intervalGroups);
        return finalResponse;
    }

    public Map<String, List<NewsArticle>> groupByInterval(List<NewsArticle> articles, int intervalValue, String intervalUnit) {
        Map<String, List<NewsArticle>> grouped = new HashMap<>();

        // Convert publishedAt to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);

        for (NewsArticle article : articles) {
            LocalDateTime publishedDateTime = LocalDateTime.parse(article.getPublishedAt(), formatter);
            LocalDateTime intervalStart = calculateIntervalStart(currentTime, publishedDateTime, intervalValue, intervalUnit);

            String intervalKey = intervalStart.toString();

            // Group articles
            grouped.computeIfAbsent(intervalKey, k -> new ArrayList<>()).add(article);
        }

        // Sort each group of articles within the interval from latest to oldest
        for (List<NewsArticle> articleList : grouped.values()) {
            articleList.sort((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt()));
        }

        return grouped;
    }

    public LocalDateTime calculateIntervalStart(LocalDateTime currentTime, LocalDateTime publishedDateTime, int intervalValue, String intervalUnit) {
        // Determine the start time of the interval for this article
        ChronoUnit unit;
        switch (intervalUnit.toLowerCase()) {
            case "minutes":
                unit = ChronoUnit.MINUTES;
                break;
            case "hours":
                unit = ChronoUnit.HOURS;
                break;
            case "days":
                unit = ChronoUnit.DAYS;
                break;
            case "weeks":
                unit = ChronoUnit.WEEKS;
                break;
            case "months":
                unit = ChronoUnit.MONTHS;
                break;
            case "years":
                unit = ChronoUnit.YEARS;
                break;
            default:
                unit = ChronoUnit.HOURS; // Default to hours if not specified
                intervalValue = 12; // Default interval
                break;
        }

        long difference = unit.between(publishedDateTime, currentTime) / intervalValue * intervalValue;
        return currentTime.minus(difference, unit);
    }
}
