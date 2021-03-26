package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;

import java.util.List;

public interface IngredientService {
    void seedIngredients();

    List<IngredientServiceModel> getAllIngredients();

    boolean nameExists(String name);

    void addIngredient(IngredientServiceModel ingredientServiceModel);

    IngredientServiceModel findIngredientById(String id);

    void editIngredient(IngredientServiceModel ingredientServiceModel);
}
