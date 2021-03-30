package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, String> {

    Optional <Cocktail> getByName(String name);

}
