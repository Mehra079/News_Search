package com.project.newsproject.controller;

import com.project.newsproject.model.IntervalGroup;
import com.project.newsproject.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:4200") // Adjust the frontend URL as needed
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/search")
    public Map<String, Object> searchNews(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "12") int interval,
            @RequestParam(required = false, defaultValue = "hours") String intervalUnit,
            @RequestParam(required = false, defaultValue = "false") boolean offlineMode) {

        // Call the service layer to get the news grouped by interval
        return newsService.getNews(keyword, interval, intervalUnit, offlineMode);
    }
}
