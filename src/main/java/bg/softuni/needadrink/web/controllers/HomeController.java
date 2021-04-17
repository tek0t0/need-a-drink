package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.domain.models.views.CocktailSearchViewModel;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.web.anotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HomeController {


    private final CocktailService cocktailService;

    @Autowired
    public HomeController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;

    }


    @GetMapping("/")
    @PageTitle("Welcome")
    public String index(Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public String home(Model model) {
        CocktailSearchViewModel cocktailSearchViewModel = this.cocktailService.getCocktailOfTheDay();
        model.addAttribute("cocktailSearchViewModel", cocktailSearchViewModel);
        return "home";
    }
}
