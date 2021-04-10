package bg.softuni.needadrink.web;

import bg.softuni.needadrink.config.CloudinaryConfiguration;
import bg.softuni.needadrink.domain.entities.ArticleEntity;
import bg.softuni.needadrink.init.DBInit;
import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.service.CloudinaryService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ArticlesControllerTest {

    private static final String ARTICLE_CONTROLLER_PREFIX = "/articles";
    private static final String FAV_ICON_SUFIX = "?favicon=https%3A%2F%2Ficons-for-free.com%2Ficonfiles%2Fpng%2F512%2Fbeverage%2Bcocktail%2Bdrink%2Bicon-1320195452071512367.png";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;
//

//


//    @BeforeEach
//    public void setup(){
//        ArticleEntity articleEntity = new ArticleEntity()
//                .setContent("test123")
//                .setDescription("test123")
//                .setAddedOn(LocalDate.now())
//                .setTitle("test123");
//        this.articleRepository.save(articleEntity);
//    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    void showAllArticlesIfAny() throws Exception {
        this.mockMvc.perform(get(ARTICLE_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/all-articles"))
                .andExpect(model().attributeExists("allArticles"));
    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addArticleRedirectsAndDoesNOtAddArticleToTheRepoWhenThereAreMistakesInBinding() throws Exception {
        long count = this.articleRepository.count();
        this.mockMvc.perform(post(ARTICLE_CONTROLLER_PREFIX + "/add")
                .param("title", "1")
                .param("img", "img")
                .with(csrf()))
                .andExpect(flash().attributeExists("articleAddBindingModel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ARTICLE_CONTROLLER_PREFIX + "/add" + FAV_ICON_SUFIX));
        Assertions.assertEquals(count, this.articleRepository.count());
    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addArticle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ARTICLE_CONTROLLER_PREFIX + "/add")
                .param("title", "test title").
                        param("coverImgUrl", "test.jpg").
                        param("description", "Description test").
                        param("addedOn", "ANY").
                        param("content", "some test content")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addArticleRedirectsAndDoesNOtAddArticleToTheRepoWhenTheNameExists() throws Exception {
        long count = this.articleRepository.count();
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        this.mockMvc.perform(post(ARTICLE_CONTROLLER_PREFIX + "/add")
                .param("title", articleEntity.getTitle()).
                        param("coverImgUrl", "test.jpg").
                        param("description", "Description test").
                        param("addedOn", "ANY").
                        param("content", "some test content")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ARTICLE_CONTROLLER_PREFIX + "/add" + FAV_ICON_SUFIX));
        Assertions.assertEquals(count, this.articleRepository.count());
    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void detailsArticleRedirectWhenNOArticleWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/details/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));

    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void detailsArticle() throws Exception {
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/details/{id}", articleEntity.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(view().name("article/details-article"));

    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleEdit() throws Exception {
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/edit/{id}", articleEntity.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleId"))
                .andExpect(view().name("article/edit-article"));

    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleEditErrorWhenNoArticleWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/edit/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));


    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleEditRedirectAfterEdit() throws Exception {
        long count = this.articleRepository.count();
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        this.mockMvc.perform(post(ARTICLE_CONTROLLER_PREFIX + "/edit/{id}", articleEntity.getId())
                .param("title", "Whats new in mixology123").
                        param("coverImgUrl", "test.jpg").
                        param("description", "Description test").
                        param("addedOn", "ANY").
                        param("content", "some test content")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/details/" + articleEntity.getId() + FAV_ICON_SUFIX));
        Assertions.assertEquals(count, this.articleRepository.count());
    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleDelete() throws Exception {
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/delete/{id}", articleEntity.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleId"))
                .andExpect(view().name("article/delete-article"));

    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleDeleteErrorWhenNoArticleWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/delete/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));

    }

    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleDeleteConfirm() throws Exception {
        long count = this.articleRepository.count();
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        this.mockMvc.perform(post(ARTICLE_CONTROLLER_PREFIX + "/delete/{id}", articleEntity.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/all" + FAV_ICON_SUFIX));
    }


}