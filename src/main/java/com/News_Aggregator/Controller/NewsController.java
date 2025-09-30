package com.News_Aggregator.Controller;

import com.News_Aggregator.Entity.NewsArticle;
import com.News_Aggregator.Service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8080", "http://127.0.0.1:5500"}) // Adjust for your frontend server (e.g., live-server on port 5500)
public class NewsController {
    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/latest")
    public ResponseEntity<List<NewsArticle>> getLatest(@RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.ok(service.getLatestArticles(limit));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<NewsArticle>> getByCategory(@PathVariable String category) {
        try {
            return ResponseEntity.ok(service.getByCategory(category));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // New: Manual fetch endpoint (call once to populate DB)
    @PostMapping("/fetch/{category}")
    public ResponseEntity<String> fetchCategory(@PathVariable String category) {
        try {
            List<NewsArticle> newsArticles = service.fetchFromNewsAPI(category);
            return ResponseEntity.ok("Fetched and saved total " +newsArticles.size() +" of "+ category + " news successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
  