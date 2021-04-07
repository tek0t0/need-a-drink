package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("select a from Article a order by a.addedOn")
    List<Article>findAllOrderByDate();

    Optional<Object> findByTitle(String title);
}
