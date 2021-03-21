package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.binding.ArticleAddBindingModel;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.service.ArticleService;
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
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ModelMapper modelMapper;

    public ArticleController(ArticleService articleService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        List<ArticleServiceModel> allArticles = this.articleService.getAllArticles();
        model.addAttribute("allArticles", allArticles);
        return "article/all-articles";
    }

    @GetMapping("/add")
    public String add(Model model) {
        if(!model.containsAttribute("articleAddBindingModel")){
            model.addAttribute("articleAddBindingModel", new ArticleAddBindingModel());
            model.addAttribute("titleExists",false);
        }
        return "article/add-article";
    }

    @PostMapping("/add")
    public String addConfirm(
            @Valid ArticleAddBindingModel articleAddBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("articleAddBindingModel", articleAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.articleAddBindingModel",
                    bindingResult);
            return "redirect:/articles/add";
        }

        if(articleService.titleExists(articleAddBindingModel.getTitle())){
            redirectAttributes.addFlashAttribute("articleAddBindingModel", articleAddBindingModel);
            redirectAttributes.addFlashAttribute("titleExists", true);
            return "redirect:/articles/add";
        }

        ArticleServiceModel articleServiceModel = modelMapper.map(articleAddBindingModel, ArticleServiceModel.class);
        articleService.addArticle(articleServiceModel);

        return "redirect:/articles/all";
    }

    @GetMapping("/details/{id}")
    public String articleDetails(@PathVariable String id, Model model){
        model.addAttribute("article", this.articleService.findArticleById(id));
        return "article/details-article";
    }
}
