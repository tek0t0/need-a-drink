package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.binding.CocktailInitBindingModel;
import bg.softuni.needadrink.domain.models.binding.IngredientBindingModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.domain.models.views.AllCocktailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.error.CommingSoonException;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;
    private final ModelMapper modelMapper;
    private final IngredientService ingredientService;
    private final UserService userService;

    public CocktailController(CocktailService cocktailService, ModelMapper modelMapper, IngredientService ingredientService, UserController userController, UserService userService) {
        this.cocktailService = cocktailService;
        this.modelMapper = modelMapper;
        this.ingredientService = ingredientService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public String allCocktails(Model model) {
        List<AllCocktailsViewModel> allCocktails = cocktailService.getAllCocktails()
                .stream()
                .map(c -> modelMapper.map(c, AllCocktailsViewModel.class))
                .collect(Collectors.toList());
        model.addAttribute("allCocktails", allCocktails);

        return "cocktail/all-cocktails";
    }

    @GetMapping("/details/{id}")
    public String cocktailDetails(@PathVariable String id, Model model, Principal principal) {
        CocktailDetailsViewModel viewModel = modelMapper.map(this.cocktailService.getCocktailById(id), CocktailDetailsViewModel.class);
        model.addAttribute("cocktailViewModel", viewModel);
        model.addAttribute("ingredients", this.ingredientService.findAllByCocktailId(id));
        model.addAttribute("alreadyInFavorite", false);
        if(this.userService.cocktailIsInFavorites(id, principal.getName())){
            model.addAttribute("alreadyInFavorite", true);
        }
        return "cocktail/details-cocktail";
    }

    @GetMapping("/add")
    public String addCocktail(Model model) {
        if (!model.containsAttribute("cocktailInitBindingModel")) {
            model.addAttribute("cocktailInitBindingModel", new CocktailInitBindingModel());
            model.addAttribute("nameExists", false);
            model.addAttribute("allIngredients", this.ingredientService
                    .getAllIngredients()
                    .stream().map(i -> modelMapper.map(i, IngredientBindingModel.class))
                    .collect(Collectors.toList()));
        }

        return "cocktail/add-cocktail";
    }

    @PostMapping("/addIngredient/{id}")
    public String addIngredient(@PathVariable String id,
                                Model model,
                                CocktailInitBindingModel cocktailInitBindingModel) {


        cocktailInitBindingModel
                .getIngredients()
                .add(modelMapper.map(ingredientService.findIngredientById(id), IngredientBindingModel.class));
        model.addAttribute("cocktailInitBindingModel", cocktailInitBindingModel);
        model.addAttribute("allIngredients", this.ingredientService.getAllWithoutAdded(cocktailInitBindingModel));


        return "cocktail/add-cocktail";
    }

    @PostMapping("/add")
    public String addCocktailConfirm(@Valid CocktailInitBindingModel cocktailInitBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("cocktailInitBindingModel", cocktailInitBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cocktailInitBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("allIngredients", this.ingredientService.getAllWithoutAdded(cocktailInitBindingModel));
            return "redirect:/cocktails/add";
        }

        if (this.cocktailService.nameExists(cocktailInitBindingModel)) {
            redirectAttributes.addFlashAttribute("cocktailInitBindingModel", cocktailInitBindingModel);
            redirectAttributes.addFlashAttribute("nameExists", true);
            return "redirect:/cocktails/add";

        }

        this.cocktailService.addCocktail(cocktailInitBindingModel);
        return "redirect:/cocktails/all";
    }

    @GetMapping("/myCocktails")
    public String favoriteCocktails(Principal principal, Model model) throws UserPrincipalNotFoundException {

        model.addAttribute("allCocktails", this.cocktailService.getFavoriteCocktails(principal.getName()));
        return "cocktail/favorite-cocktails";

    }

    @PostMapping("/addToFavorites/{id}")
    public String addToFavorites(@PathVariable String id, Principal principal) throws UserPrincipalNotFoundException {
        this.userService.addCocktailToUserFavorites(principal.getName(), id);

        return "redirect:/cocktails/myCocktails";
    }

    @PostMapping("/removeFromFavorites/{id}")
    public String removeFromFavorites(@PathVariable String id, Principal principal) throws UserPrincipalNotFoundException {
        this.userService.removeCocktailToUserFavorites(principal.getName(), id);

        return "redirect:/cocktails/myCocktails";
    }

    @GetMapping("/cocktails/edit/{id}")
    public String editCocktail(@PathVariable String id){
        throw new CommingSoonException();
    }

    @PostMapping("/delete/{id}")
    public String deleteCocktailConfirm(@PathVariable String id) {
        this.cocktailService.deleteById(id);
        return "redirect:/cocktails/all";
    }


}
