package com.project.newsproject;

import com.project.newsproject.controller.NewsController;
import com.project.newsproject.model.IntervalGroup;
import com.project.newsproject.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsControllerTest {

    @InjectMocks
    private NewsController newsController;

    @Mock
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchNewsSuccessfully() {
        String keyword = "apple";
        int interval = 12;
        String intervalUnit = "hours";
        boolean offlineMode = false;

        // Prepare mock response from the service
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("intervalGroups", Arrays.asList(new IntervalGroup("2024-10-28T12:00:00Z", 2, null)));

        when(newsService.getNews(keyword, interval, intervalUnit, offlineMode)).thenReturn(mockResponse);

        Map<String, Object> response = newsController.searchNews(keyword, interval, intervalUnit, offlineMode);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(newsService, times(1)).getNews(keyword, interval, intervalUnit, offlineMode);
    }

    @Test
    void testSearchNewsWithDefaultIntervalAndUnit() {
        String keyword = "apple";

        // Prepare mock response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("intervalGroups", Arrays.asList(new IntervalGroup("2024-10-28T12:00:00Z", 1, null)));

        when(newsService.getNews(keyword, 12, "hours", false)).thenReturn(mockResponse);

        Map<String, Object> response = newsController.searchNews(keyword, 0, null, false);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(newsService, times(1)).getNews(keyword, 12, "hours", false);
    }

    @Test
    void testSearchNewsOfflineMode() {
        String keyword = "apple";
        int interval = 24;
        String intervalUnit = "hours";
        boolean offlineMode = true;

        // Prepare mock response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("intervalGroups", Arrays.asList(new IntervalGroup("2024-10-28T12:00:00Z", 3, null)));

        when(newsService.getNews(keyword, interval, intervalUnit, offlineMode)).thenReturn(mockResponse);

        Map<String, Object> response = newsController.searchNews(keyword, interval, intervalUnit, offlineMode);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(newsService, times(1)).getNews(keyword, interval, intervalUnit, offlineMode);
    }

    @Test
    void testSearchNewsWithNoKeyword() {
        String keyword = "";
        int interval = 12;
        String intervalUnit = "hours";
        boolean offlineMode = false;

        // Prepare mock response for empty keyword
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("intervalGroups", Arrays.asList());

        when(newsService.getNews(keyword, interval, intervalUnit, offlineMode)).thenReturn(mockResponse);

        Map<String, Object> response = newsController.searchNews(keyword, interval, intervalUnit, offlineMode);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(newsService, times(1)).getNews(keyword, interval, intervalUnit, offlineMode);
    }
}
