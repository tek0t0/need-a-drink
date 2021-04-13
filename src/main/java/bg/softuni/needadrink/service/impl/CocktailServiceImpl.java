package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.entities.Ingredient;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;
import bg.softuni.needadrink.error.CocktailNotFoundException;
import bg.softuni.needadrink.error.EmptyCocktailDataBaseError;
import bg.softuni.needadrink.service.CocktailShuffler;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.util.ValidatorUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CocktailServiceImpl implements CocktailService {
    @Value("classpath:init/cocktails.json")
    Resource cocktailsFile;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final IngredientRepository ingredientRepository;
    private final CocktailRepository cocktailRepository;
    private final IngredientService ingredientService;
    private final ValidatorUtil validatorUtil;
    private final UserRepository userRepository;
    private final LogService logService;
    private final CocktailShuffler cocktailShuffler;

    private CocktailSearchViewModel cocktailSearchViewModel;

    @Autowired
    public CocktailServiceImpl(Gson gson, ModelMapper modelMapper, IngredientRepository ingredientRepository, CocktailRepository cocktailRepository, IngredientService ingredientService, ValidatorUtil validatorUtil, UserRepository userRepository, LogService logService, CocktailShuffler cocktailShuffler) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.ingredientService = ingredientService;
        this.validatorUtil = validatorUtil;
        this.userRepository = userRepository;
        this.logService = logService;
        this.cocktailShuffler = cocktailShuffler;
    }


    @Override
    public CocktailSearchViewModel getCocktailOfTheDay() {
        throwsExceptionIfDBIsEmpty();

        if (this.cocktailSearchViewModel == null) {
            Cocktail cocktail = this.cocktailRepository.findAll().get(0);
            CocktailSearchViewModel viewModel = modelMapper.map(cocktail, CocktailSearchViewModel.class);
            viewModel.setIngredientsNames(cocktail.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList()));

            return viewModel;
        }
        return this.cocktailSearchViewModel;
    }

    @Override
    @Transactional
    public void seedCocktails() throws IOException {
        String content;

        content = String.join("", Files.readAllLines(Path.of(cocktailsFile.getURI())));
        CocktailInitBindingModel[] cocktailModels = this.gson.fromJson(content, CocktailInitBindingModel[].class);
        for (CocktailInitBindingModel cocktailModel : cocktailModels) {
            if (this.cocktailRepository.getByName(cocktailModel.getName()).isPresent()) {
                LogServiceModel logServiceModel = new LogServiceModel();
                logServiceModel.setUsername("ADMIN");
                logServiceModel.setDescription("Cocktail already exists!.");
                this.logService.seedLogInDB(logServiceModel);
            } else {
                List<IngredientBindingModel> ingredientModels = cocktailModel.getIngredients();
                for (IngredientBindingModel ingredient : ingredientModels) {
                    if (this.ingredientRepository.getByName(ingredient.getName()).isEmpty()) {
                        this.ingredientService.addIngredient(modelMapper.map(ingredient, IngredientServiceModel.class));
                    }
                }

                if (this.validatorUtil.isValid(cocktailModel)) {
                    Cocktail cocktail = this.modelMapper.map(cocktailModel, Cocktail.class);

                    cocktail.getIngredients().clear();
                    for (IngredientBindingModel ingredientModel : ingredientModels) {
                        cocktail.addIngredient((this.ingredientService.findByName(ingredientModel.getName())));
                    }

                    if (cocktailModel.getImgUrl().isEmpty()) {
                        cocktail.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
                    }

                    this.cocktailRepository.saveAndFlush(cocktail);

                    //TODO: Override Log constructor with all the params!!! OR USE ASPECTS TO LOG!!!

                    LogServiceModel logServiceModel = new LogServiceModel();
                    logServiceModel.setUsername("ADMIN");
                    logServiceModel.setDescription("Cocktail added.");
                    this.logService.seedLogInDB(logServiceModel);


                } else {
                    LogServiceModel logServiceModel = new LogServiceModel();
                    logServiceModel.setUsername("ADMIN");
                    logServiceModel.setDescription("Failed to add cocktail.");
                    this.logService.seedLogInDB(logServiceModel);
                }
            }
        }
    }

    @Override
    public List<CocktailServiceModel> getAllCocktails() {
        List<CocktailServiceModel> allCocktailsModelsList = this.cocktailRepository
                .finaAllOrderByName()
                .stream()
                .map(c -> {
                    CocktailServiceModel cocktailServiceModel = this.modelMapper.map(c, CocktailServiceModel.class);
                    List<String> ingredients = c.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList());
                    cocktailServiceModel.setIngredientsNames(ingredients);
                    return cocktailServiceModel;
                })
                .collect(Collectors.toList());


        return allCocktailsModelsList;
    }

    @Override
    public CocktailServiceModel getCocktailById(String id) {
        Cocktail byId = getCocktail(id);
        return this.modelMapper.map(byId, CocktailServiceModel.class);
    }

    @Override
    public boolean nameExists(String name) {
        return this.cocktailRepository.getByName(name).isPresent();

    }

    @Override
    public void addCocktail(CocktailInitBindingModel cocktailInitBindingModel) {

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Cocktail added.");

        this.logService.seedLogInDB(logServiceModel);

        this.cocktailRepository.saveAndFlush(this.modelMapper.map(cocktailInitBindingModel, Cocktail.class));
    }

    @Override
    public List<CocktailDetailsViewModel> getFavoriteCocktails(String principalName) {
        UserEntity userEntity = this.userRepository.findByEmail(principalName)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        List<Cocktail> favoriteCocktails = userEntity.getFavoriteCocktails();
        return favoriteCocktails
                .stream()
                .map(c -> this.modelMapper.map(c, CocktailDetailsViewModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteById(String id) {

        Cocktail cocktail = getCocktail(id);

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Cocktail deleted.");
        this.logService.seedLogInDB(logServiceModel);

        this.cocktailRepository.delete(cocktail);
    }

    private Cocktail getCocktail(String id) {
        return this.cocktailRepository.findById(id)
                .orElseThrow(() -> new CocktailNotFoundException(Constants.COCKTAIL_ID_NOT_FOUND));
    }

    @Scheduled(cron = "0 * * ? * *") //every minute
//    @Scheduled(cron = "0 0 0 * * ?") //every day at 24:00
    protected void getRandomCocktail() {
        throwsExceptionIfDBIsEmpty();

        List<CocktailSearchViewModel> collect = this.cocktailRepository
                .findAll().stream()
                .map(c -> {
                    CocktailSearchViewModel cocktailSearchViewModel = this.modelMapper.map(c, CocktailSearchViewModel.class);
                    cocktailSearchViewModel.setIngredientsNames(c.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList()));
                    return cocktailSearchViewModel;
                })
                .collect(Collectors.toList());
        this.cocktailShuffler.shuffle(collect);
        this.cocktailSearchViewModel = collect.get(0);
    }

    private void throwsExceptionIfDBIsEmpty() {
        if (this.cocktailRepository.count() == 0) {
            throw new EmptyCocktailDataBaseError(Constants.COCKTAIL_DB_IS_EMPTY);
        }
    }


}
