package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    void seedIngredients();

    List<IngredientServiceModel> getAllIngredients();

    boolean nameExists(String name);

    void addIngredient(IngredientServiceModel ingredientServiceModel);

    IngredientServiceModel findIngredientById(String id);

    void editIngredient(IngredientServiceModel ingredientServiceModel);

    boolean newNameExists(IngredientServiceModel ingredientServiceModel);

    void deleteIngredient(String id);

    Ingredient findByName(String name);
}
