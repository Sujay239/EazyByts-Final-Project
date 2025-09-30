package com.News_Aggregator;

import com.News_Aggregator.Service.NewsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// ...
@SpringBootApplication
@EnableScheduling  // If using scheduled tasks
public class NewsAggregatorApplication implements CommandLineRunner {
    private final NewsService newsService;

    public NewsAggregatorApplication(NewsService newsService) {
        this.newsService = newsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsAggregatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Fetch initial data on startup (once)
        newsService.fetchFromNewsAPI("general");
        newsService.fetchFromNewsAPI("technology");
        // Add more categories
    }
}
  