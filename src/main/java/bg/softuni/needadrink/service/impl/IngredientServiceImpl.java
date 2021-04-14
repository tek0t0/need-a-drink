package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.IngredientEntity;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        for (IngredientBindingModel ingredientBindingModel : ingredientBindingModels) {
            if (this.ingredientRepository.getByName(ingredientBindingModel.getName()).isPresent()) {
                LogServiceModel logServiceModel = new LogServiceModel();
                logServiceModel.setUsername("ADMIN");
                logServiceModel.setDescription("Ingredient name already exists.");
                this.logService.seedLogInDB(logServiceModel);
            } else {
                if (this.validatorUtil.isValid(ingredientBindingModel)) {
                    addDefaultImgIngredient(ingredientBindingModel);
                    IngredientEntity ingredientEntity = this.modelMapper.map(ingredientBindingModel, IngredientEntity.class);
                    this.ingredientRepository.saveAndFlush(ingredientEntity);

                    LogServiceModel logServiceModel = new LogServiceModel();
                    logServiceModel.setUsername("ADMIN");
                    logServiceModel.setDescription("Ingredient added.");

                    this.logService.seedLogInDB(logServiceModel);

                } else {
                    LogServiceModel logServiceModel = new LogServiceModel();
                    logServiceModel.setUsername("ADMIN");
                    logServiceModel.setDescription("Failed to add ingredient." + ingredientBindingModel);

                    this.logService.seedLogInDB(logServiceModel);
                }
            }


        }


    }

    protected void addDefaultImgIngredient(IngredientBindingModel ingredientBindingModel) {
        if (ingredientBindingModel.getImgUrl().isEmpty()) {
            ingredientBindingModel.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
        }
    }

    @Override
    public List<IngredientServiceModel> getAllIngredients() {

        return this.ingredientRepository.findAllOrderByName()
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
        if (ingredientServiceModel.getImgUrl() == null) {
            ingredientServiceModel.setImgUrl(Constants.DEFAULT_INGREDIENT_IMG_URL);
        }
        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Ingredient added.");

        this.logService.seedLogInDB(logServiceModel);

        this.ingredientRepository.saveAndFlush(modelMapper.map(ingredientServiceModel, IngredientEntity.class));
    }

    @Override
    public IngredientServiceModel findIngredientById(String id) {
        IngredientEntity ingredientEntity = this.ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_NOT_FOUND));
        return modelMapper.map(ingredientEntity, IngredientServiceModel.class);
    }

    @Override
    public IngredientServiceModel editIngredient(IngredientServiceModel ingredientServiceModel) {
        IngredientEntity ingredientEntity = this.ingredientRepository.findById(ingredientServiceModel.getId())
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_ID_NOT_FOUND));
        ingredientEntity
                .setName(ingredientServiceModel.getName())
                .setImgUrl(ingredientServiceModel.getImgUrl())
                .setDescription(ingredientServiceModel.getDescription());


        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Ingredient edited.");

        this.logService.seedLogInDB(logServiceModel);
        this.ingredientRepository.saveAndFlush(ingredientEntity);
        return modelMapper.map(ingredientEntity, IngredientServiceModel.class);
    }

    @Override
    public boolean newNameExists(IngredientServiceModel ingredientServiceModel) {
        if (this.ingredientRepository.getByName(ingredientServiceModel.getName()).isEmpty()) {
            return false;
        }

        IngredientEntity ingredientEntity = this.ingredientRepository.getByName(ingredientServiceModel.getName())
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_NAME_NOT_FOUND));
        return !ingredientEntity.getId().equals(ingredientServiceModel.getId());
    }

    @Override
    public void deleteIngredient(String id) {
        entityExists(id);

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription("Ingredient deleted.");

        this.logService.seedLogInDB(logServiceModel);

        this.ingredientRepository.deleteById(id);
    }


    @Override
    public IngredientEntity findByName(String name) {
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
                .findAllExceptAdded(ingredientsNames)
                .stream().map(i -> modelMapper.map(i, IngredientBindingModel.class))
                .collect(Collectors.toList());
    }


    private void entityExists(String id) {
        IngredientEntity ingredientEntity = this.ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(Constants.INGREDIENT_ID_NOT_FOUND));
    }


}
