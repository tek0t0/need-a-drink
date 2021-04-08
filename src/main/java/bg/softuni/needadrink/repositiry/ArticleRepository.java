package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query("select a from ArticleEntity a order by a.addedOn")
    List<ArticleEntity>findAllOrderByDate();

    Optional<Object> findByTitle(String title);
}
