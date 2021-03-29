package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;

import java.util.List;

public interface CocktailService {
    void seedCocktails();

    List<CocktailServiceModel> getAllCocktails();

    CocktailServiceModel getCocktailById(String id);
}
