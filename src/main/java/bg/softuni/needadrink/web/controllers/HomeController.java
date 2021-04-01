package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.views.CocktailDetailsViewModel;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.service.CocktailService;
import bg.softuni.needadrink.service.UserService;
import bg.softuni.needadrink.web.anotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HomeController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CocktailService cocktailService;

    @Autowired
    public HomeController(UserService userService, ModelMapper modelMapper, CocktailService cocktailService) {
        this.userService = userService;
        this.cocktailService = cocktailService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
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
    public String home(Principal principal, Model model) {
        String email = principal.getName();


        CocktailDetailsViewModel cocktailDetailsViewModel = this.cocktailService.getCocktailOfTheDay();
        model.addAttribute("cocktailDetailsViewModel", cocktailDetailsViewModel);

        return "home";
    }
}
