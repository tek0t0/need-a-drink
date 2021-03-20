package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/all")
    public String getAll(Model model){
        List<ArticleServiceModel> allArticles = this.articleService.getAllArticles();
        model.addAttribute("allArticles", allArticles);
        return "article/all-articles";
    }

    @GetMapping("/add")
    public String add(){
        return "article/add-article";
    }

    @PostMapping("/add")
    public String addConfirm(){
        return "redirect:articles/all";
    }
}
