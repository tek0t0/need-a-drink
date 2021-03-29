package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.error.CocktailNameAlreadyExists;
import bg.softuni.needadrink.error.Constants;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.InvalidJsonException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class CocktailServiceImpl implements CocktailService {
    @Value("classpath:init/cocktails.json") Resource cocktailsFile;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final IngredientRepository ingredientRepository;
    private final CocktailRepository cocktailRepository;
    private final IngredientService ingredientService;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public CocktailServiceImpl(Gson gson, ModelMapper modelMapper, IngredientRepository ingredientRepository, CocktailRepository cocktailRepository, IngredientService ingredientService, ValidatorUtil validatorUtil) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.ingredientService = ingredientService;
        this.validatorUtil = validatorUtil;
    }


    @Override
    @Transactional
    public void seedCocktails() {
        String content = null;
        try {
            content = String.join("", Files.readAllLines(Path.of(cocktailsFile.getURI())));
            CocktailInitBindingModel[] cocktailModels = this.gson.fromJson(content, CocktailInitBindingModel[].class);
            for (CocktailInitBindingModel cocktailModel : cocktailModels) {
                if(this.cocktailRepository.getByName(cocktailModel.getName()).isPresent()){
                    throw new CocktailNameAlreadyExists(Constants.COCKTAIL_ALREADY_EXISTS);
                }
                List<IngredientBindingModel> ingredientModels = cocktailModel.getIngredients();
                for (IngredientBindingModel ingredient : ingredientModels) {
                    if(ingredientRepository.getByName(ingredient.getName()).isEmpty()){
                        ingredientService.addIngredient(modelMapper.map(ingredient, IngredientServiceModel.class));
                    }
                }

                if(this.validatorUtil.isValid(cocktailModel)){
                    Cocktail cocktail = this.modelMapper.map(cocktailModel, Cocktail.class);

                    cocktail.getIngredients().clear();
                    for (IngredientBindingModel ingredientModel : ingredientModels) {
                        cocktail.addIngredient((ingredientService.findByName(ingredientModel.getName())));
                    }

                    if(cocktailModel.getImgUrl().isEmpty()){
                        cocktail.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
                    }

                    this.cocktailRepository.saveAndFlush(cocktail);
                } else {
                    throw new InvalidJsonException(Constants.INVALID_JSON_DATA_ERROR);
                }


            }
        } catch (IOException | CocktailNameAlreadyExists e) {
            //TODO add custom exception
            e.printStackTrace();
        }

    }
}
