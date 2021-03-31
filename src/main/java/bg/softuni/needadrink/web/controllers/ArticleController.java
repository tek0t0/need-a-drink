package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.binding.ArticleAddBindingModel;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.web.anotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PageTitle("All Articles")
    public String getAll(Model model) {
        List<ArticleServiceModel> allArticles = this.articleService.getAllArticles();
        model.addAttribute("allArticles", allArticles);
        return "article/all-articles";
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    @PageTitle("Add Article")
    public String add(Model model) {
        if (!model.containsAttribute("articleAddBindingModel")) {
            model.addAttribute("articleAddBindingModel", new ArticleAddBindingModel());
            model.addAttribute("titleExists", false);
        }
        return "article/add-article";
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String addConfirm(@Valid ArticleAddBindingModel articleAddBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("articleAddBindingModel", articleAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.articleAddBindingModel",
                    bindingResult);
            return "redirect:/articles/add";
        }

        if (articleService.titleExists(articleAddBindingModel.getTitle())) {
            redirectAttributes.addFlashAttribute("articleAddBindingModel", articleAddBindingModel);
            redirectAttributes.addFlashAttribute("titleExists", true);
            return "redirect:/articles/add";
        }

        ArticleServiceModel articleServiceModel = modelMapper.map(articleAddBindingModel, ArticleServiceModel.class);
        articleService.addArticle(articleServiceModel);

        return "redirect:/articles/all";
    }

    @GetMapping("/details/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PageTitle("Article")
    public String articleDetails(@PathVariable String id, Model model) {
        model.addAttribute("article", this.articleService.findArticleById(id));
        return "article/details-article";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    @PageTitle("Edit Article")
    public String articleEdit(@PathVariable String id, Model model) {
        model.addAttribute("article", this.articleService.findArticleById(id));
        model.addAttribute("articleId", id);
        return "article/edit-article";
    }

    @PostMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String articleEditConfirm(@PathVariable String id,
                                     ArticleAddBindingModel articleAddBindingModel) {
        //TODO:Add validation
        ArticleServiceModel serviceModel = modelMapper.map(articleAddBindingModel, ArticleServiceModel.class);
        this.articleService.editArticle(id, serviceModel);

        return "redirect:/articles/details/" + id;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    @PageTitle("Delete Article")
    public String articleDelete(@PathVariable String id, Model model) {
        model.addAttribute("article", this.articleService.findArticleById(id));
        model.addAttribute("articleId", id);
        return "article/delete-article";
    }

    @PostMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    public String articleDeleteConfirm(@PathVariable String id) {
        this.articleService.deleteById(id);
        return "redirect:/articles/all";
    }
}
