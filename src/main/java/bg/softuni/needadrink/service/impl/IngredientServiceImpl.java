package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.error.Constants;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.service.IngredientService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Value("classpath:init/ingredients.json") Resource ingredientsFile;
    private final IngredientRepository ingredientRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, Gson gson, ModelMapper modelMapper) {
        this.ingredientRepository = ingredientRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedIngredients() {
        String content = null;
        try {
            content = String.join("", Files.readAllLines(Path.of(ingredientsFile.getURI())));
            IngredientBindingModel[] ingredientBindingModels = this.gson.fromJson(content, IngredientBindingModel[].class);
            for (IngredientBindingModel bindingModel : ingredientBindingModels) {
                Ingredient ingredient = this.modelMapper.map(bindingModel, Ingredient.class);
                this.ingredientRepository.saveAndFlush(ingredient);
            }
        } catch (IOException e) {
            //TODO add custom exception
            e.printStackTrace();
        }

    }
}
