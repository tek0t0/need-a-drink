package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.service.UserService;
import bg.softuni.needadrink.web.anotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class HomeController {

    private final UserService userService;
    private final ArticleService articleService;
    private final ModelMapper modelMapper;

    public HomeController(UserService userService, ArticleService articleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/")
    @PageTitle("Welcome")
    public String index(){
        return "index";
    }

    @GetMapping("/home")
    @PageTitle("Home")
    public String home(Principal principal, Model model){
        String email = principal.getName();
// TODO: greet with Full Name       userService.getUser(email);
        List<ArticleServiceModel> articles = this.articleService
                .findLatestArticles();

        model.addAttribute("latestArticles", articles);

        return "home";
    }
}
