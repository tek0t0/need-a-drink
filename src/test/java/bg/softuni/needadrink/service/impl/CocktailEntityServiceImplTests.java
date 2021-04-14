package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.CocktailEntity;
import bg.softuni.needadrink.domain.entities.IngredientEntity;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.error.CocktailNotFoundException;
import bg.softuni.needadrink.error.EmptyCocktailDataBaseError;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.service.CocktailShuffler;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CocktailEntityServiceImplTests {

    private CocktailEntity testCocktailEntity1, testCocktailEntity2;

    private IngredientEntity testIngredientEntity1, testIngredientEntity2;

    private CocktailService serviceToTest;

    @Mock
    private IngredientRepository mockIngredientRepository;

    @Mock
    private IngredientService mockIngredientService;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private CocktailShuffler mockCocktailShuffler;

    @Mock
    CocktailRepository mockCocktailRepository;

    @Mock
    LogService mockLogService;

    @Mock
    ValidatorUtil validatorUtil;


    @BeforeEach
    public void init() {

        testIngredientEntity1 = new IngredientEntity();
        testIngredientEntity2 = new IngredientEntity();

        testIngredientEntity1
                .setName("ing1")
                .setDescription("ing_desc1")
                .setImgUrl("ing_img1");
        testIngredientEntity2
                .setName("ing2")
                .setDescription("ing_desc2")
                .setImgUrl("ing_img2");

        testCocktailEntity1 = new CocktailEntity();
        testCocktailEntity1
                .setName("cocktail1")
                .setImgUrl("image1")
                .setDescription("description1")
                .setPreparation("preparation1")
                .setIngredients(Set.of(testIngredientEntity1, testIngredientEntity2));

        testCocktailEntity2 = new CocktailEntity();
        testCocktailEntity2
                .setName("cocktail2")
                .setImgUrl("image2")
                .setDescription("description2")
                .setPreparation("preparation2")
                .setIngredients(Set.of(testIngredientEntity1, testIngredientEntity2));


        serviceToTest = new CocktailServiceImpl(
                new Gson(),
                new ModelMapper(),
                mockIngredientRepository,
                mockCocktailRepository,
                mockIngredientService,
                validatorUtil,
                mockUserRepository,
                mockLogService,
                mockCocktailShuffler);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(mockCocktailRepository);
    }

    @Test
    void testGetAllCocktails() {
        when(mockCocktailRepository.finaAllOrderByName()).thenReturn(List.of(testCocktailEntity1, testCocktailEntity2));

        List<CocktailServiceModel> allCocktails = serviceToTest.getAllCocktails();

        Assertions.assertEquals(2, allCocktails.size());

        CocktailServiceModel model1 = allCocktails.get(0);
        CocktailServiceModel model2 = allCocktails.get(1);

        Assertions.assertEquals(testCocktailEntity1.getName(), model1.getName());
        Assertions.assertEquals(testCocktailEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testCocktailEntity1.getPreparation(), model1.getPreparation());
        Assertions.assertEquals(testCocktailEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(2, model1.getIngredientsNames().size());


        Assertions.assertEquals(testCocktailEntity2.getName(), model2.getName());
        Assertions.assertEquals(testCocktailEntity2.getImgUrl(), model2.getImgUrl());
        Assertions.assertEquals(testCocktailEntity2.getPreparation(), model2.getPreparation());
        Assertions.assertEquals(testCocktailEntity2.getDescription(), model2.getDescription());
        Assertions.assertEquals(2, model2.getIngredientsNames().size());
    }

    @Test
    void testGetCocktailByIdReturn() {
        Mockito.when(mockCocktailRepository.findById("A")).thenReturn(Optional.of((testCocktailEntity1)));

        CocktailServiceModel model1 = serviceToTest.getCocktailById("A");

        Assertions.assertEquals(testCocktailEntity1.getName(), model1.getName());
        Assertions.assertEquals(testCocktailEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testCocktailEntity1.getPreparation(), model1.getPreparation());
        Assertions.assertEquals(testCocktailEntity1.getDescription(), model1.getDescription());
    }

    @Test
    void testGetCocktailByIdThrowsException() {
        Assertions.assertThrows(CocktailNotFoundException.class, () -> serviceToTest.getCocktailById("C"));
    }

    @Test
    void testNameExists() {
        when(mockCocktailRepository.getByName("cocktail_1")).thenReturn(Optional.of(testCocktailEntity1));

        Assertions.assertTrue(serviceToTest.nameExists("cocktail_1"));
    }

    @Test
    void testGetFavoriteCocktails() throws UserPrincipalNotFoundException {
        UserEntity userEntity = new UserEntity()
                .setImgUrl("A")
                .setPassword("A")
                .setBirthDate(LocalDate.now())
                .setEmail("A@A")
                .setFavoriteCocktails(List.of(testCocktailEntity1, testCocktailEntity2));

        when(mockUserRepository.findByEmail("A@A")).thenReturn(Optional.of(userEntity));

        List<CocktailDetailsViewModel> favoriteCocktails = serviceToTest.getFavoriteCocktails("A@A");

        Assertions.assertEquals(2, favoriteCocktails.size());


        CocktailDetailsViewModel model1 = favoriteCocktails.get(0);
        CocktailDetailsViewModel model2 = favoriteCocktails.get(1);

        Assertions.assertEquals(testCocktailEntity1.getName(), model1.getName());
        Assertions.assertEquals(testCocktailEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testCocktailEntity1.getDescription(), model1.getDescription());

        Assertions.assertEquals(testCocktailEntity2.getName(), model2.getName());
        Assertions.assertEquals(testCocktailEntity2.getImgUrl(), model2.getImgUrl());
        Assertions.assertEquals(testCocktailEntity2.getDescription(), model2.getDescription());
    }

    @Test
    void testDeleteByIdWorks() {

        Mockito.when(mockCocktailRepository.findById("A")).
                thenReturn(Optional.of(testCocktailEntity1));
        serviceToTest.deleteById("A");
    }

    @Test
    void testDeleteByIdThrowsException() {

        Assertions.assertThrows(CocktailNotFoundException.class, () -> serviceToTest.deleteById("C"));
    }

    @Test
    void testAddCocktail() {
        CocktailInitBindingModel cocktailInitBindingModel = new CocktailInitBindingModel()
                .setDescription("AAA")
                .setImgUrl("AAAA")
                .setName("A")
                .setPreparation("AAAAA").setIngredients(List.of());
        serviceToTest.addCocktail(cocktailInitBindingModel);
    }

    @Test
    void testEmptyDBThrowsException() {
        Assertions.assertThrows(EmptyCocktailDataBaseError.class, () -> serviceToTest.getCocktailOfTheDay());
    }
}
