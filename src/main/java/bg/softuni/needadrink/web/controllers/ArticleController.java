package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.binding.ArticleAddBindingModel;
import bg.softuni.needadrink.domain.models.binding.CommentBindingModel;
import bg.softuni.needadrink.domain.models.service.ArticleServiceModel;
import bg.softuni.needadrink.domain.models.service.CommentServiceModel;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.service.UserService;
import bg.softuni.needadrink.util.Constants;
import bg.softuni.needadrink.web.anotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, ModelMapper modelMapper, UserService userService) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("All Articles")
    public String getAll(Model model) {
        List<ArticleServiceModel> allArticles = this.articleService.getAllArticles();
        model.addAttribute("allArticles", allArticles);
        return "article/all-articles";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @PageTitle("Add Article")
    public String add(Model model) {
        if (!model.containsAttribute("articleAddBindingModel")) {
            model.addAttribute("articleAddBindingModel", new ArticleAddBindingModel());
            model.addAttribute("titleExists", false);
        }
        return "article/add-article";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
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

        if (articleAddBindingModel.getCoverImgUrl().isEmpty()) {
            articleAddBindingModel.setCoverImgUrl(Constants.DEFAULT_ARTICLE_IMAGE_URL);
        }

        ArticleServiceModel articleServiceModel = modelMapper.map(articleAddBindingModel, ArticleServiceModel.class);
        articleService.addArticle(articleServiceModel);

        return "redirect:/articles/all";
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Article")
    public String articleDetails(@PathVariable String id, Model model, Principal principal) {
        if(!model.containsAttribute("commentBindingModel")){
            model.addAttribute("commentBindingModel", new CommentBindingModel());
        }

        ArticleServiceModel article = this.articleService.findArticleById(id);
        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());
        model.addAttribute("commentsCount", article.getComments().size());
        model.addAttribute("currentUserImg", this.userService.findUserByEmail(principal.getName()).getImgUrl());
        model.addAttribute("articleId", id);
        return "article/details-article";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @PageTitle("Edit Article")
    public String articleEdit(@PathVariable String id, Model model) {
        model.addAttribute("article", this.articleService.findArticleById(id));
        model.addAttribute("articleId", id);
        return "article/edit-article";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String articleEditConfirm(@PathVariable String id,
                                     ArticleAddBindingModel articleAddBindingModel) {
        ArticleServiceModel serviceModel = modelMapper.map(articleAddBindingModel, ArticleServiceModel.class);
        if (serviceModel.getCoverImgUrl() == null) {
            serviceModel.setCoverImgUrl(Constants.DEFAULT_ARTICLE_IMAGE_URL);
        }
        this.articleService.editArticle(id, serviceModel);

        return "redirect:/articles/details/" + id;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @PageTitle("Delete Article")
    public String articleDelete(@PathVariable String id, Model model) {
        model.addAttribute("article", this.articleService.findArticleById(id));
        model.addAttribute("articleId", id);
        return "article/delete-article";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String articleDeleteConfirm(@PathVariable String id) {
        this.articleService.deleteById(id);
        return "redirect:/articles/all";
    }

    @PostMapping("/{id}/add/comment")
    @PreAuthorize("isAuthenticated()")
    public String articleAddComment(@PathVariable String id,
                                    CommentBindingModel commentBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentBindingModel", commentBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentBindingModel",
                    bindingResult);
            return "redirect:/articles/details/" + id;
        }

        this.articleService.saveComment(id, commentBindingModel, principal.getName());

        return "redirect:/articles/details/" + id;
    }
}
