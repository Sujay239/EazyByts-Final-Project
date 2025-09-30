package com.News_Aggregator.Repository;

import com.News_Aggregator.Entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    Optional<NewsArticle> findByUrl(String url); // For duplicates
    List<NewsArticle> findByCategoryOrderByPublishedAtDesc(String category);

    @Query("SELECT n FROM NewsArticle n ORDER BY n.publishedAt DESC")
    List<NewsArticle> findLatestArticles(org.springframework.data.domain.Pageable pageable);
    // Use Pageable for large limits if needed
}
  