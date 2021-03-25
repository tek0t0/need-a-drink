package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {
    Optional<Object> getByName(String name);
}
