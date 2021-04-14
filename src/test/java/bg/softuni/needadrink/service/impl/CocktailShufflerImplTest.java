package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;
import bg.softuni.needadrink.service.CocktailShuffler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CocktailShufflerImplTest {

    private CocktailShuffler cocktailShuffler;

    @Test
    void testShuffler(){
        cocktailShuffler = new CocktailShufflerImpl();

        CocktailSearchViewModel model1 = new CocktailSearchViewModel();
        CocktailSearchViewModel model2 = new CocktailSearchViewModel();
        CocktailSearchViewModel model3 = new CocktailSearchViewModel();
        CocktailSearchViewModel model4 = new CocktailSearchViewModel();
        model1.setDescription("test1").setId("1").setImgUrl("testUrl1").setName("test1").setPreparation("test1");
        model1.setDescription("test2").setId("2").setImgUrl("testUrl2").setName("test2").setPreparation("test2");
        model1.setDescription("test3").setId("3").setImgUrl("testUrl3").setName("test3").setPreparation("test3");
        model1.setDescription("test4").setId("4").setImgUrl("testUrl4").setName("test4").setPreparation("test4");

        List<CocktailSearchViewModel> cocktailsBefore = List.of(model1, model2,model3,model4);
        List<CocktailSearchViewModel> cocktailsAfter = new ArrayList<>(cocktailsBefore);
        Assertions.assertEquals(cocktailsBefore, cocktailsAfter);
        cocktailShuffler.shuffle(cocktailsAfter);
        Assertions.assertNotEquals(cocktailsBefore, cocktailsAfter);


    }
}
