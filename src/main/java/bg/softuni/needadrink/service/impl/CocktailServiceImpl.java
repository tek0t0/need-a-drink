package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.domain.models.views.AllCocktailsViewModel;
import bg.softuni.needadrink.error.CocktailNameAlreadyExists;
import bg.softuni.needadrink.error.CocktailNotFoundException;
import bg.softuni.needadrink.error.Constants;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
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
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CocktailServiceImpl implements CocktailService {
    @Value("classpath:init/cocktails.json") Resource cocktailsFile;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final IngredientRepository ingredientRepository;
    private final CocktailRepository cocktailRepository;
    private final IngredientService ingredientService;
    private final ValidatorUtil validatorUtil;
    private final UserRepository userRepository;

    @Autowired
    public CocktailServiceImpl(Gson gson, ModelMapper modelMapper, IngredientRepository ingredientRepository, CocktailRepository cocktailRepository, IngredientService ingredientService, ValidatorUtil validatorUtil, UserRepository userRepository) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.ingredientService = ingredientService;
        this.validatorUtil = validatorUtil;
        this.userRepository = userRepository;
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

    @Override
    public List<CocktailServiceModel> getAllCocktails() {

        return this.cocktailRepository
                .findAll()
                .stream()
                .map(c->modelMapper.map(c, CocktailServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CocktailServiceModel getCocktailById(String id) {
        Cocktail byId = this.cocktailRepository.findById(id)
                .orElseThrow(()-> new CocktailNotFoundException(Constants.COCKTAIL_ID_NOT_FOUND));
        return modelMapper.map(byId, CocktailServiceModel.class);
    }

    @Override
    public boolean nameExists(CocktailInitBindingModel cocktailInitBindingModel) {
        return this.cocktailRepository.getByName(cocktailInitBindingModel.getName()).isPresent();

    }

    @Override
    public void addCocktail(CocktailInitBindingModel cocktailInitBindingModel) {
        this.cocktailRepository.saveAndFlush(modelMapper.map(cocktailInitBindingModel, Cocktail.class));
    }

    @Override
    public List<AllCocktailsViewModel> getFavoriteCocktails(String principalName) throws UserPrincipalNotFoundException {
        Optional<UserEntity> userEntity = this.userRepository.findByEmail(principalName);
        if(userEntity.isPresent()){
            List<Cocktail> favoriteCocktails = userEntity.get().getFavoriteCocktails();
            return favoriteCocktails.stream().map(c->modelMapper.map(c, AllCocktailsViewModel.class)).collect(Collectors.toList());
        } else {
            throw new UserPrincipalNotFoundException(Constants.USER_ID_NOT_FOUND);
        }
    }
}
