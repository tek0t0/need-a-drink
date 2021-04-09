package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.domain.models.views.IngredientViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IngredientService {
    void seedIngredients() throws IOException;

    List<IngredientServiceModel> getAllIngredients();

    boolean nameExists(String name);

    void addIngredient(IngredientServiceModel ingredientServiceModel);

    IngredientServiceModel findIngredientById(String id);

    IngredientServiceModel editIngredient(IngredientServiceModel ingredientServiceModel);

    boolean newNameExists(IngredientServiceModel ingredientServiceModel);

    void deleteIngredient(String id);

    Ingredient findByName(String name);

    List<IngredientViewModel> findAllByCocktailId(String id);

    List<IngredientBindingModel> getAllWithoutAdded(CocktailInitBindingModel cocktailInitBindingModel);
}
