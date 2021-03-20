package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query(value = "select * from needadrink_db.articles as a order by added_on desc limit 3",nativeQuery = true)
    List<Article>findLatestArticles();
}
