package bg.softuni.needadrink.service;


import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;

import java.util.List;

public interface CocktailShuffler {
    void shuffle(List<CocktailSearchViewModel> cocktails);
}
