package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.CocktailServiceModel;
import bg.softuni.needadrink.domain.models.service.IngredientServiceModel;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.domain.models.views.AllCocktailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.error.CocktailNameAlreadyExists;
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
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
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

    private CocktailDetailsViewModel cocktailDetailsViewModel;

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
    public CocktailDetailsViewModel getCocktailOfTheDay() {
        if(this.cocktailRepository.count() == 0){
            throw new EmptyCocktailDataBaseError();
        }

        if(this.cocktailDetailsViewModel == null){
            return modelMapper.map(this.cocktailRepository.findAll().get(0), CocktailDetailsViewModel.class);
        }
        return this.cocktailDetailsViewModel;
    }

    @Override
    @Transactional
    public void seedCocktails() throws IOException {
        String content;

        content = String.join("", Files.readAllLines(Path.of(cocktailsFile.getURI())));
        CocktailInitBindingModel[] cocktailModels = this.gson.fromJson(content, CocktailInitBindingModel[].class);
        for (CocktailInitBindingModel cocktailModel : cocktailModels) {
            if (this.cocktailRepository.getByName(cocktailModel.getName()).isPresent()) {
                throw new CocktailNameAlreadyExists(Constants.COCKTAIL_ALREADY_EXISTS);
            }
            List<IngredientBindingModel> ingredientModels = cocktailModel.getIngredients();
            for (IngredientBindingModel ingredient : ingredientModels) {
                if (ingredientRepository.getByName(ingredient.getName()).isEmpty()) {
                    ingredientService.addIngredient(modelMapper.map(ingredient, IngredientServiceModel.class));
                }
            }

            if (this.validatorUtil.isValid(cocktailModel)) {
                Cocktail cocktail = this.modelMapper.map(cocktailModel, Cocktail.class);

                cocktail.getIngredients().clear();
                for (IngredientBindingModel ingredientModel : ingredientModels) {
                    cocktail.addIngredient((ingredientService.findByName(ingredientModel.getName())));
                }

                if (cocktailModel.getImgUrl().isEmpty()) {
                    cocktail.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
                }

                this.cocktailRepository.saveAndFlush(cocktail);
            } else {
                LogServiceModel logServiceModel = new LogServiceModel();
                logServiceModel.setUsername("ADMIN");
                logServiceModel.setDescription("Failed to add cocktail.");
                logServiceModel.setTime(LocalDateTime.now());

                this.logService.seedLogInDB(logServiceModel);
            }
        }
    }

    @Override
    public List<CocktailServiceModel> getAllCocktails() {

        return this.cocktailRepository
                .findAll()
                .stream()
                .map(c -> modelMapper.map(c, CocktailServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CocktailServiceModel getCocktailById(String id) {
        Cocktail byId = this.cocktailRepository.findById(id)
                .orElseThrow(() -> new CocktailNotFoundException(Constants.COCKTAIL_ID_NOT_FOUND));
        return modelMapper.map(byId, CocktailServiceModel.class);
    }

    @Override
    public boolean nameExists(CocktailInitBindingModel cocktailInitBindingModel) {
        return this.cocktailRepository.getByName(cocktailInitBindingModel.getName()).isPresent();

    }

    @Override
    public void addCocktail(CocktailInitBindingModel cocktailInitBindingModel) {

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Cocktail added.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.cocktailRepository.saveAndFlush(modelMapper.map(cocktailInitBindingModel, Cocktail.class));
    }

    @Override
    public List<AllCocktailsViewModel> getFavoriteCocktails(String principalName) {
        UserEntity userEntity = this.userRepository.findByEmail(principalName)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        List<Cocktail> favoriteCocktails = userEntity.getFavoriteCocktails();
        return favoriteCocktails
                .stream()
                .map(c -> modelMapper.map(c, AllCocktailsViewModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteById(String id) {

        Cocktail cocktail = this.cocktailRepository.findById(id)
                .orElseThrow(() -> new CocktailNotFoundException(Constants.COCKTAIL_ID_NOT_FOUND));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Cocktail deleted.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.cocktailRepository.delete(cocktail);
    }
    @Scheduled(cron = "0 * * ? * *") //every minute
//    @Scheduled(cron = "0 0 0 * * ?") //every day at 24:00
    protected void getRandomCocktail() {
        if(this.cocktailRepository.count() == 0){
            throw new EmptyCocktailDataBaseError();
        }

        List<CocktailDetailsViewModel> collect = this.cocktailRepository
                .findAll().stream()
                .map(c -> modelMapper.map(c, CocktailDetailsViewModel.class))
                .collect(Collectors.toList());
        this.cocktailShuffler.shuffle(collect);
        this.cocktailDetailsViewModel = collect.get(0);
    }



}
