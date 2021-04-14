package bg.softuni.needadrink.repositiry;

import bg.softuni.needadrink.domain.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, String> {

    Optional<IngredientEntity> getByName(String name);

    @Query("select i from IngredientEntity i join i.usedIn c where c.id = :id")
    List<IngredientEntity>findAllByCocktailId(@Param(value = "id") String id);

    @Query("select i from IngredientEntity i where i.name not in :names")
    List<IngredientEntity> findAllExceptAdded(@Param(value = "names") List<String> ingredientsNames);

    @Query("select i from IngredientEntity i order by i.name")
    List<IngredientEntity> findAllOrderByName();
}
