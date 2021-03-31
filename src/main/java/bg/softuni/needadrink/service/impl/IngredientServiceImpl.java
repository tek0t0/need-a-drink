package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.domain.models.views.IngredientViewModel;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
import bg.softuni.needadrink.error.IngredientNotFoundException;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.InvalidJsonException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Value("classpath:init/ingredients.json")
    Resource ingredientsFile;
    private final IngredientRepository ingredientRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final LogService logService;


    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil, LogService logService) {
        this.ingredientRepository = ingredientRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.logService = logService;
    }

    @Override
    public void seedIngredients() throws IOException {
        String content;

        content = String.join("", Files.readAllLines(Path.of(ingredientsFile.getURI())));
        IngredientBindingModel[] ingredientBindingModels = this.gson.fromJson(content, IngredientBindingModel[].class);
        for (IngredientBindingModel bindingModel : ingredientBindingModels) {

            if (this.validatorUtil.isValid(bindingModel)) {
                addDefaultImgIngredient(bindingModel);
                Ingredient ingredient = this.modelMapper.map(bindingModel, Ingredient.class);
                this.ingredientRepository.saveAndFlush(ingredient);
            }

        }


    }

    protected void addDefaultImgIngredient(IngredientBindingModel bindingModel) {
        if (bindingModel.getImgUrl().isEmpty()) {
            bindingModel.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
        }
    }

    @Override
    public List<IngredientServiceModel> getAllIngredients() {

        return this.ingredientRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(i -> this.modelMapper.map(i, IngredientServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean nameExists(String name) {
        return ingredientRepository.getByName(name).isPresent();
    }

    @Override
    public void addIngredient(IngredientServiceModel ingredientServiceModel) {
        if (ingredientServiceModel.getImgUrl() == null || ingredientServiceModel.getImgUrl().isEmpty()) {
            ingredientServiceModel.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
        }

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Ingredient added.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.ingredientRepository.saveAndFlush(modelMapper.map(ingredientServiceModel, Ingredient.class));
    }

    @Override
    public IngredientServiceModel findIngredientById(String id) {
        Ingredient ingredient = this.ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_NOT_FOUND));
        return modelMapper.map(ingredient, IngredientServiceModel.class);
    }

    @Override
    public void editIngredient(IngredientServiceModel ingredientServiceModel) {
        Ingredient ingredient = this.ingredientRepository.findById(ingredientServiceModel.getId())
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_ID_NOT_FOUND));
        ingredient
                .setName(ingredientServiceModel.getName())
                .setImgUrl(ingredientServiceModel.getImgUrl());


        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Ingredient edited.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);
        this.ingredientRepository.saveAndFlush(ingredient);
    }

    @Override
    public boolean newNameExists(IngredientServiceModel ingredientServiceModel) {
        if (ingredientRepository.getByName(ingredientServiceModel.getName()).isEmpty()) {
            return false;
        }

        Ingredient ingredient = ingredientRepository.getByName(ingredientServiceModel.getName())
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_NAME_NOT_FOUND));
        return !ingredient.getId().equals(ingredientServiceModel.getId());
    }

    @Override
    public void deleteIngredient(String id) {

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Ingredient deleted.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.ingredientRepository.deleteById(id);
    }

    @Override
    public Ingredient findByName(String name) {
        return this.ingredientRepository.getByName(name)
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_NOT_FOUND));
    }

    @Override
    public List<IngredientViewModel> findAllByCocktailId(String id) {
        return this.ingredientRepository.findAllByCocktailId(id)
                .stream()
                .map(i -> modelMapper.map(i, IngredientViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<IngredientBindingModel> getAllWithoutAdded(CocktailInitBindingModel cocktailInitBindingModel) {
        List<String> ingredientsNames = new ArrayList<>();
        cocktailInitBindingModel.getIngredients().forEach(i -> ingredientsNames.add(i.getName()));
        return this.ingredientRepository
                .finadAllExeptAdded(ingredientsNames)
                .stream().map(i -> modelMapper.map(i, IngredientBindingModel.class))
                .collect(Collectors.toList());
    }


}
