package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface CocktailService {

    CocktailSearchViewModel getCocktailOfTheDay();

    void seedCocktails() throws IOException;

    List<CocktailServiceModel> getAllCocktails();

    CocktailServiceModel getCocktailById(String id);

    boolean nameExists(String name);

    void addCocktail(CocktailInitBindingModel cocktailInitBindingModel);

    List<CocktailDetailsViewModel> getFavoriteCocktails(String principalName) throws UserPrincipalNotFoundException;

    void deleteById(String id);
}
