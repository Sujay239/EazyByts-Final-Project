package com.News_Aggregator.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "news_articles")
@NoArgsConstructor
public class NewsArticle {
    // Getters and setters (generate via IDE or add manually)
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String source;

    @Column(unique = true) // Prevent duplicates
    private String url;

    private String imageUrl;

    @Column(name = "published_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime publishedAt;

    private String category;

    public NewsArticle(String title, String content, String source, String url,
                       String urlToImage, String publishedAt, String category) {
        this.title = title;
        this.content = content;
        this.source = source;
        this.url = url;
        this.imageUrl = urlToImage;

        // Safer parsing
        try {
            this.publishedAt = java.time.OffsetDateTime.parse(publishedAt).toLocalDateTime();
        } catch (Exception e) {
            this.publishedAt = LocalDateTime.now(); // fallback
        }

        this.category = category; // ✅ correctly assign passed category
    }
}
