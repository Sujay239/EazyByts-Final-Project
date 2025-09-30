package com.News_Aggregator.DTO;// NewsApiResponse.java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {
    // Getters
    @JsonProperty("status")
    private String status;

    @JsonProperty("articles")
    private List<NewsApiArticle> articles;

}



