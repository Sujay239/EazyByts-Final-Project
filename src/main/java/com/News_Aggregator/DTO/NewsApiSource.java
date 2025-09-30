package com.News_Aggregator.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// NewsApiSource.java
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiSource {
    @JsonProperty("name")
    private String name;
}
