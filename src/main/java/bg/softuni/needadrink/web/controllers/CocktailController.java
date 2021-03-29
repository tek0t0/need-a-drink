package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.views.AllCocktailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.service.IngredientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;
    private final ModelMapper modelMapper;
    private final IngredientService ingredientService;

    public CocktailController(CocktailService cocktailService, ModelMapper modelMapper, IngredientService ingredientService) {
        this.cocktailService = cocktailService;
        this.modelMapper = modelMapper;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/all")
    public String allCocktails(Model model){
        List<AllCocktailsViewModel> allCocktails = cocktailService.getAllCocktails()
                .stream()
                .map(c->modelMapper.map(c, AllCocktailsViewModel.class))
                .collect(Collectors.toList());
        model.addAttribute("allCocktails", allCocktails);

        return "cocktail/all-cocktails";
    }

    @GetMapping("/details/{id}")
    public String cocktailDetails(@PathVariable String id, Model model){
        CocktailDetailsViewModel viewModel = modelMapper.map(this.cocktailService.getCocktailById(id), CocktailDetailsViewModel.class);
        model.addAttribute("cocktailViewModel", viewModel);
        model.addAttribute("ingredients", this.ingredientService.findAllByCocktailId(id));

        return "cocktail/details-cocktail";
    }



}
