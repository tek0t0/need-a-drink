package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;

import java.util.List;

public interface CocktailShuffler {
    void shuffle(List<CocktailDetailsViewModel> cocktails);
}
