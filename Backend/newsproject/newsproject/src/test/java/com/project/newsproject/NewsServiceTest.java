package com.project.newsproject;

import com.project.newsproject.model.NewsApiResponse;
import com.project.newsproject.model.NewsArticle;
import com.project.newsproject.model.IntervalGroup;
import com.project.newsproject.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${newsapi.Key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNewsOfflineModeReturnsCachedArticles() {
        String keyword = "apple";
        List<NewsArticle> cachedArticles = new ArrayList<>();
        cachedArticles.add(new NewsArticle("Title 1", "Description 1", "http://url1.com", "2024-10-28T12:00:00Z"));
        newsService.cache.put(keyword, cachedArticles); // Manually adding to cache

        Map<String, Object> response = newsService.getNews(keyword, 12, "hours", true);

        assertNotNull(response);
        List<IntervalGroup> intervalGroups = (List<IntervalGroup>) response.get("intervalGroups");
        assertEquals(1, intervalGroups.size());
        assertEquals(1, intervalGroups.get(0).getCount());
        assertEquals("Title 1", intervalGroups.get(0).getArticles().get(0).getTitle());
    }

    @Test
    void testGetNewsFetchesArticlesFromAPI() {
        String keyword = "apple";
        List<NewsArticle> articles = Arrays.asList(
                new NewsArticle("Title 1", "Description 1", "http://url1.com", "2024-10-28T12:00:00Z"),
                new NewsArticle("Title 2", "Description 2", "http://url2.com", "2024-10-28T11:00:00Z")
        );

        NewsApiResponse mockResponse = new NewsApiResponse();
        mockResponse.setArticles(articles);

        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.eq(NewsApiResponse.class)))
                .thenReturn(mockResponse);

        Map<String, Object> response = newsService.getNews(keyword, 12, "hours", false);

        assertNotNull(response);
        List<IntervalGroup> intervalGroups = (List<IntervalGroup>) response.get("intervalGroups");
        assertEquals(1, intervalGroups.size());
        assertEquals(2, intervalGroups.get(0).getCount());
        assertEquals("Title 1", intervalGroups.get(0).getArticles().get(0).getTitle());
        assertEquals("Title 2", intervalGroups.get(0).getArticles().get(1).getTitle());
    }

    @Test
    void testGetNewsNoArticlesFound() {
        String keyword = "notfound";
        when(restTemplate.getForObject(ArgumentMatchers.anyString(), ArgumentMatchers.eq(NewsApiResponse.class)))
                .thenReturn(new NewsApiResponse());

        Map<String, Object> response = newsService.getNews(keyword, 12, "hours", false);

        assertNotNull(response);
        List<IntervalGroup> intervalGroups = (List<IntervalGroup>) response.get("intervalGroups");
        assertEquals(0, intervalGroups.size());
    }

    @Test
    void testGroupByInterval() {
        List<NewsArticle> articles = Arrays.asList(
                new NewsArticle("Title 1", "Description 1", "http://url1.com", "2024-10-28T12:00:00Z"),
                new NewsArticle("Title 2", "Description 2", "http://url2.com", "2024-10-28T11:00:00Z"),
                new NewsArticle("Title 3", "Description 3", "http://url3.com", "2024-10-27T12:00:00Z")
        );

        Map<String, List<NewsArticle>> grouped = newsService.groupByInterval(articles, 12, "hours");

        assertNotNull(grouped);
        assertEquals(2, grouped.size()); // Expected to group into 2 intervals
    }

    @Test
    void testCalculateIntervalStart() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime publishedTime = currentTime.minusHours(5);
        LocalDateTime intervalStart = newsService.calculateIntervalStart(currentTime, publishedTime, 12, "hours");

        assertNotNull(intervalStart);
        assertTrue(intervalStart.isBefore(currentTime));
    }

    @Test
    void testGroupByIntervalWithEmptyList() {
        List<NewsArticle> articles = new ArrayList<>();
        Map<String, List<NewsArticle>> grouped = newsService.groupByInterval(articles, 12, "hours");

        assertNotNull(grouped);
        assertTrue(grouped.isEmpty());
    }
}
