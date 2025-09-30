package com.News_Aggregator.DTO;// NewsApiArticle.java (nested DTO)
import com.News_Aggregator.DTO.NewsApiSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiArticle {
    // Getters
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description; // Use as content

    @JsonProperty("url")
    private String url;

    @JsonProperty("urlToImage")
    private String urlToImage;

    @JsonProperty("source")
    private NewsApiSource source;

    @JsonProperty("publishedAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String publishedAt;

}