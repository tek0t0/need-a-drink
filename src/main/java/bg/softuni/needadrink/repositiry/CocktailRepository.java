package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.CocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<CocktailEntity, String> {

    Optional <CocktailEntity> getByName(String name);

    @Query("select c from CocktailEntity c order by c.name")
    List<CocktailEntity>finaAllOrderByName();

}
