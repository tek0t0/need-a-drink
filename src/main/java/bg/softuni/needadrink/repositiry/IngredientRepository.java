package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {

    Optional<Ingredient> getByName(String name);

    @Query("select i from Ingredient i join i.usedIn c where c.id = :id")
    List<Ingredient>findAllByCocktailId(@Param(value = "id") String id);

    @Query("select i from Ingredient i where i.name not in :names")
    List<Ingredient> findAllExceptAdded(@Param(value = "names") List<String> ingredientsNames);

    @Query("select i from Ingredient i order by i.name")
    List<Ingredient> findAllOrderByName();
}
