package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.entities.Ingredient;

import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.domain.models.views.IngredientViewModel;
import bg.softuni.needadrink.error.IngredientNotFoundException;
import bg.softuni.needadrink.repositiry.IngredientRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceImplTest {

    private Ingredient testIngredient1, testIngredient2;

    private IngredientServiceModel ingredientServiceModel;

    private IngredientServiceImpl serviceToTest;


    @Mock
    private IngredientRepository mockIngredientRepository;

    @Mock
    LogService mockLogService;

    @Mock
    ValidatorUtil validatorUtil;


    @BeforeEach
    public void init() {


        testIngredient1 = new Ingredient();
        testIngredient1
                .setName("Ing1")
                .setDescription("test1")
                .setImgUrl("url1")
                .setUsedIn(List.of())
                .setId("A");

        testIngredient2 = new Ingredient();
        testIngredient2
                .setName("Ing2")
                .setDescription("test2")
                .setImgUrl("url2")
                .setUsedIn(List.of())
                .setId("B");

        ingredientServiceModel = new IngredientServiceModel();
        ingredientServiceModel
                .setDescription("AAAA")
                .setImgUrl("AAA")
                .setName("AA")
                .setId("A");

        serviceToTest = new IngredientServiceImpl(
                mockIngredientRepository,
                new Gson(),
                new ModelMapper(),
                validatorUtil,
                mockLogService);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(mockIngredientRepository);
    }

    @Test
    void testGetAllIngredients(){
        when(mockIngredientRepository.findAllOrderByName()).thenReturn(List.of(testIngredient1, testIngredient2));

        List<IngredientServiceModel> allIngredients = serviceToTest.getAllIngredients();

        Assertions.assertEquals(2, allIngredients.size());

        IngredientServiceModel model1 = allIngredients.get(0);
        IngredientServiceModel model2 = allIngredients.get(1);

        Assertions.assertEquals(testIngredient1.getName(), model1.getName());
        Assertions.assertEquals(testIngredient1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredient1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredient1.getId(), model1.getId());

        Assertions.assertEquals(testIngredient2.getName(), model2.getName());
        Assertions.assertEquals(testIngredient2.getDescription(), model2.getDescription());
        Assertions.assertEquals(testIngredient2.getImgUrl(), model2.getImgUrl());
        Assertions.assertEquals(testIngredient2.getId(), model2.getId());
    }

    @Test
    void testNameExists() {
        when(mockIngredientRepository.getByName("Ing1")).thenReturn(Optional.of(testIngredient1));

        Assertions.assertTrue(serviceToTest.nameExists("Ing1"));
    }

    @Test
    void testAddIngredient(){

        serviceToTest.addIngredient(ingredientServiceModel);
    }

    @Test
    void testFindIngredientById(){
        Mockito.when(mockIngredientRepository.findById("A")).thenReturn(Optional.of((testIngredient1)));

        IngredientServiceModel model1 = serviceToTest.findIngredientById("A");

        Assertions.assertEquals(testIngredient1.getName(), model1.getName());
        Assertions.assertEquals(testIngredient1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredient1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredient1.getId(), model1.getId());
    }

    @Test
    void testFindIngredientByIdThrowException() {
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.findIngredientById("C"));
    }

    @Test
    void testEditIngredient() {

        Mockito.when(mockIngredientRepository.findById("A")).thenReturn(Optional.of(testIngredient1));

        IngredientServiceModel model1 = serviceToTest.editIngredient(this.ingredientServiceModel);

        Assertions.assertEquals(testIngredient1.getName(), model1.getName());
        Assertions.assertEquals(testIngredient1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredient1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredient1.getId(), model1.getId());
    }

    @Test
    void testEditIngredientThrowsException() {
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.editIngredient(ingredientServiceModel));
    }

    @Test
    void testDeleteIngredient(){

        Mockito.when(mockIngredientRepository.findById("A")).
                thenReturn(Optional.of(testIngredient1));
        serviceToTest.deleteIngredient("A");
    }

    @Test
    void testDeleteIngredientThrowsException(){
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.deleteIngredient("A"));
    }

    @Test
    void testFindByName(){
        Mockito.when(mockIngredientRepository.getByName("A")).thenReturn(Optional.of(testIngredient1));

        Ingredient model1 = serviceToTest.findByName("A");

        Assertions.assertEquals(testIngredient1.getName(), model1.getName());
        Assertions.assertEquals(testIngredient1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredient1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredient1.getId(), model1.getId());

    }

    @Test
    void testFindByNameThrowsException(){
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.findByName("A"));
    }

    @Test
    void testFindAllByCocktailId(){
        Cocktail cocktail = new Cocktail()
                .setName("test1")
                .setDescription("test1")
                .setImgUrl("test1")
                .setPreparation("test1")
                .setIngredients(Set.of(testIngredient1, testIngredient2));
        cocktail.setId("A");

        Mockito.when(mockIngredientRepository.findAllByCocktailId("A")).thenReturn(List.of(testIngredient1,testIngredient2));

        List<IngredientViewModel> all = serviceToTest.findAllByCocktailId("A");

        IngredientViewModel model1 = all.get(0);
        IngredientViewModel model2 = all.get(1);

        Assertions.assertEquals(testIngredient1.getName(), model1.getName());
        Assertions.assertEquals(testIngredient1.getDescription(), model1.getDescription());

        Assertions.assertEquals(testIngredient2.getName(), model2.getName());
        Assertions.assertEquals(testIngredient2.getDescription(), model2.getDescription());

    }



}
