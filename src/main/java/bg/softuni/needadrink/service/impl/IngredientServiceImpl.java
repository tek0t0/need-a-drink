package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.error.Constants;
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


    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.ingredientRepository = ingredientRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public void seedIngredients() {
        String content = null;
        try {
            content = String.join("", Files.readAllLines(Path.of(ingredientsFile.getURI())));
            IngredientBindingModel[] ingredientBindingModels = this.gson.fromJson(content, IngredientBindingModel[].class);
            for (IngredientBindingModel bindingModel : ingredientBindingModels) {

                if(this.validatorUtil.isValid(bindingModel)){
                    addDefaultImgIngredient(bindingModel);
                    Ingredient ingredient = this.modelMapper.map(bindingModel, Ingredient.class);
                    this.ingredientRepository.saveAndFlush(ingredient);
                } else {
                    throw new InvalidJsonException(Constants.INVALID_JSON_DATA_ERROR);
                }

            }
        } catch (IOException e) {
            //TODO add custom exception
            e.printStackTrace();
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
        this.ingredientRepository.deleteById(id);
    }

    @Override
    public Ingredient findByName(String name) {
        return this.ingredientRepository.getByName(name).orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_NOT_FOUND));
    }
}
