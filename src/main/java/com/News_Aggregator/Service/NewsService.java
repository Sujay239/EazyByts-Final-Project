package com.News_Aggregator.Service;

import com.News_Aggregator.DTO.NewsApiArticle;
import com.News_Aggregator.DTO.NewsApiResponse;
import com.News_Aggregator.Entity.NewsArticle;
import com.News_Aggregator.Repository.NewsArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Value("${news.api.key:9b5a8773c9f5419394dcaa1fb0d0e3e5}") // Fallback if not set
    private String newsApiKey;
//    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    private final NewsArticleRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsService(NewsArticleRepository repository) {
        this.repository = repository;
    }

    // Fetch and save from NewsAPI (call this separately, e.g., on startup or scheduled)
    public List<NewsArticle> fetchFromNewsAPI(String category) {
        if (newsApiKey == null || newsApiKey.equals("your-default-key")) {
            throw new IllegalArgumentException("NewsAPI key not configured in application.properties");
        }

        String url = "https://newsapi.org/v2/top-headlines?category=" + category + "&country=us&apiKey=" + newsApiKey; // Added country for more results
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                NewsApiResponse apiResponse = objectMapper.readValue(response.getBody(), NewsApiResponse.class);
                if ("ok".equals(apiResponse.getStatus()) && apiResponse.getArticles() != null) {
                    return apiResponse.getArticles().stream()
                            .map(apiArticle -> mapToNewsArticle(apiArticle, category))
                            .map(this::saveIfNew)
                            .collect(Collectors.toList());
                } else {
                    throw new RuntimeException("NewsAPI returned invalid status: " + apiResponse.getStatus());
                }
            } else {
                throw new RuntimeException("NewsAPI HTTP error: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("NewsAPI key invalid or rate limit exceeded: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching from NewsAPI: " + e.getMessage(), e);
        }
    }

    // Map API article to entity
    private NewsArticle mapToNewsArticle(NewsApiArticle apiArticle, String category) {
        return new NewsArticle(
                apiArticle.getTitle(),
                apiArticle.getDescription() != null ? apiArticle.getDescription() : "",
                apiArticle.getSource() != null ? apiArticle.getSource().getName() : "Unknown",
                apiArticle.getUrl(),
                apiArticle.getUrlToImage(),
                apiArticle.getPublishedAt(),
                category
        );
    }

    // Save only if not duplicate
    private NewsArticle saveIfNew(NewsArticle article) {
        Optional<NewsArticle> existing = repository.findByUrl(article.getUrl());
        return existing.orElseGet(() -> repository.save(article));
    }

    // Query DB for display (efficient, no API calls)
//    public List<NewsArticle> getByCategory(String category) {
//        return repository.findByCategoryOrderByPublishedAtDesc(category);
//    }

    // Query DB for display (efficient, auto-fetch if empty)
    public List<NewsArticle> getByCategory(String category) {
        List<NewsArticle> articles = repository.findByCategoryOrderByPublishedAtDesc(category);

        // If DB is empty, fetch from NewsAPI and save
        if (articles.isEmpty()) {
            System.out.println("No articles in DB, fetching from NewsAPI for category: " + category);
            articles = fetchFromNewsAPI(category);
        }

        return articles;
    }


    public List<NewsArticle> getLatestArticles(int limit) {
        return repository.findLatestArticles(PageRequest.of(0, limit));
    }

}

//https://newsapi.org/v2/top-headlines?category=category&country=india&apiKey=9b5a8773c9f5419394dcaa1fb0d0e3e5
  