package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;
import bg.softuni.needadrink.service.CocktailShuffler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CocktailShufflerImpl implements CocktailShuffler {
    @Override
    public void shuffle(List<CocktailSearchViewModel> cocktails) {
        Collections.shuffle(cocktails);
    }
}
