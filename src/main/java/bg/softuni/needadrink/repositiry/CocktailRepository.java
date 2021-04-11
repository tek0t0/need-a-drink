package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, String> {

    Optional <Cocktail> getByName(String name);

    @Query("select c from Cocktail c order by c.name")
    List<Cocktail>finaAllOrderByName();

}
