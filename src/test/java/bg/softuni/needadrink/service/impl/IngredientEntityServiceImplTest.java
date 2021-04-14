package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.CocktailEntity;
import bg.softuni.needadrink.domain.entities.IngredientEntity;

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
public class IngredientEntityServiceImplTest {

    private IngredientEntity testIngredientEntity1, testIngredientEntity2;

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


        testIngredientEntity1 = new IngredientEntity();
        testIngredientEntity1
                .setName("Ing1")
                .setDescription("test1")
                .setImgUrl("url1")
                .setUsedIn(List.of())
                .setId("A");

        testIngredientEntity2 = new IngredientEntity();
        testIngredientEntity2
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
        when(mockIngredientRepository.findAllOrderByName()).thenReturn(List.of(testIngredientEntity1, testIngredientEntity2));

        List<IngredientServiceModel> allIngredients = serviceToTest.getAllIngredients();

        Assertions.assertEquals(2, allIngredients.size());

        IngredientServiceModel model1 = allIngredients.get(0);
        IngredientServiceModel model2 = allIngredients.get(1);

        Assertions.assertEquals(testIngredientEntity1.getName(), model1.getName());
        Assertions.assertEquals(testIngredientEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredientEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredientEntity1.getId(), model1.getId());

        Assertions.assertEquals(testIngredientEntity2.getName(), model2.getName());
        Assertions.assertEquals(testIngredientEntity2.getDescription(), model2.getDescription());
        Assertions.assertEquals(testIngredientEntity2.getImgUrl(), model2.getImgUrl());
        Assertions.assertEquals(testIngredientEntity2.getId(), model2.getId());
    }

    @Test
    void testNameExists() {
        when(mockIngredientRepository.getByName("Ing1")).thenReturn(Optional.of(testIngredientEntity1));

        Assertions.assertTrue(serviceToTest.nameExists("Ing1"));
    }

    @Test
    void testAddIngredient(){

        serviceToTest.addIngredient(ingredientServiceModel);
    }

    @Test
    void testFindIngredientById(){
        Mockito.when(mockIngredientRepository.findById("A")).thenReturn(Optional.of((testIngredientEntity1)));

        IngredientServiceModel model1 = serviceToTest.findIngredientById("A");

        Assertions.assertEquals(testIngredientEntity1.getName(), model1.getName());
        Assertions.assertEquals(testIngredientEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredientEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredientEntity1.getId(), model1.getId());
    }

    @Test
    void testFindIngredientByIdThrowException() {
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.findIngredientById("C"));
    }

    @Test
    void testEditIngredient() {

        Mockito.when(mockIngredientRepository.findById("A")).thenReturn(Optional.of(testIngredientEntity1));

        IngredientServiceModel model1 = serviceToTest.editIngredient(this.ingredientServiceModel);

        Assertions.assertEquals(testIngredientEntity1.getName(), model1.getName());
        Assertions.assertEquals(testIngredientEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredientEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredientEntity1.getId(), model1.getId());
    }

    @Test
    void testEditIngredientThrowsException() {
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.editIngredient(ingredientServiceModel));
    }

    @Test
    void testDeleteIngredient(){

        Mockito.when(mockIngredientRepository.findById("A")).
                thenReturn(Optional.of(testIngredientEntity1));
        serviceToTest.deleteIngredient("A");
    }

    @Test
    void testDeleteIngredientThrowsException(){
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.deleteIngredient("A"));
    }

    @Test
    void testFindByName(){
        Mockito.when(mockIngredientRepository.getByName("A")).thenReturn(Optional.of(testIngredientEntity1));

        IngredientEntity model1 = serviceToTest.findByName("A");

        Assertions.assertEquals(testIngredientEntity1.getName(), model1.getName());
        Assertions.assertEquals(testIngredientEntity1.getDescription(), model1.getDescription());
        Assertions.assertEquals(testIngredientEntity1.getImgUrl(), model1.getImgUrl());
        Assertions.assertEquals(testIngredientEntity1.getId(), model1.getId());

    }

    @Test
    void testFindByNameThrowsException(){
        Assertions.assertThrows(IngredientNotFoundException.class, () -> serviceToTest.findByName("A"));
    }

    @Test
    void testFindAllByCocktailId(){
        CocktailEntity cocktailEntity = new CocktailEntity()
                .setName("test1")
                .setDescription("test1")
                .setImgUrl("test1")
                .setPreparation("test1")
                .setIngredients(Set.of(testIngredientEntity1, testIngredientEntity2));
        cocktailEntity.setId("A");

        Mockito.when(mockIngredientRepository.findAllByCocktailId("A")).thenReturn(List.of(testIngredientEntity1, testIngredientEntity2));

        List<IngredientViewModel> all = serviceToTest.findAllByCocktailId("A");

        IngredientViewModel model1 = all.get(0);
        IngredientViewModel model2 = all.get(1);

        Assertions.assertEquals(testIngredientEntity1.getName(), model1.getName());
        Assertions.assertEquals(testIngredientEntity1.getDescription(), model1.getDescription());

        Assertions.assertEquals(testIngredientEntity2.getName(), model2.getName());
        Assertions.assertEquals(testIngredientEntity2.getDescription(), model2.getDescription());

    }



}