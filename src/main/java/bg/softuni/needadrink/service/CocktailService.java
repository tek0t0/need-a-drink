package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.domain.models.views.AllCocktailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface CocktailService {
    void seedCocktails() throws IOException;

    List<CocktailServiceModel> getAllCocktails();

    CocktailServiceModel getCocktailById(String id);

    boolean nameExists(CocktailInitBindingModel cocktailInitBindingModel);

    void addCocktail(CocktailInitBindingModel cocktailInitBindingModel);

    List<AllCocktailsViewModel> getFavoriteCocktails(String principalName) throws UserPrincipalNotFoundException;

    void deleteById(String id);
}
