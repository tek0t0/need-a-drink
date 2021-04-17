package bg.softuni.needadrink.web;

import bg.softuni.needadrink.domain.entities.ArticleEntity;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.CommentRepository;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.repositiry.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ArticlesControllerTest extends BaseTest{

    private String testArticleId;
    private String testCommentId;
    private String testUserId;
    private String testCocktailId;
    private String testIngredientId;

    private static final String ARTICLE_CONTROLLER_PREFIX = "/articles";
    private static final String FAV_ICON_EXTENSION = "?favicon=%2Fimages%2Ffav_icon.png";

    private TestData testData;

    @Autowired
    private ModelMapper modelMapper  = new ModelMapper();

    @BeforeEach
    public void setup() {
        testData = new TestData(
                userRepository,
                logRepository,
                articleRepository,
                commentRepository,
                cocktailRepository,
                ingredientRepository,
                roleRepository

        );
        testData.init();
        testArticleId = testData.getTestArticleId();
        testCommentId = testData.getTestCommentId();
        testUserId = testData.getTestUserId();
        testCocktailId = testData.getTestCocktailId();
        testIngredientId = testData.getTestIngredientId();
    }

    @AfterEach
    public void tearDown() {
        testData.cleanUp();
    }


    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    void showAllArticlesIfAny() throws Exception {
        this.mockMvc.perform(get(ARTICLE_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/all-articles"))
                .andExpect(model().attributeExists("allArticles"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addArticleRedirectsAndDoesNOtAddArticleToTheRepoWhenThereAreMistakesInBinding() throws Exception {
        long count = this.articleRepository.count();
        this.mockMvc.perform(post(ARTICLE_CONTROLLER_PREFIX + "/add")
                .param("title", "1")
                .param("img", "img")
                .with(csrf()))
                .andExpect(flash().attributeExists("articleAddBindingModel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ARTICLE_CONTROLLER_PREFIX + "/add" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.articleRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addArticle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(ARTICLE_CONTROLLER_PREFIX + "/add")
                .param("title", "test title").
                        param("coverImgUrl", "test.jpg").
                        param("description", "Description test").
                        param("addedOn", "ANY").
                        param("content", "some test content")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Transactional
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
                .andExpect(redirectedUrl(ARTICLE_CONTROLLER_PREFIX + "/add" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.articleRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void detailsArticleRedirectWhenNOArticleWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/details/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    public void detailsArticle() throws Exception {
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/details/{id}", articleEntity.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(view().name("article/details-article"));

    }

    @Test
    @Transactional
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
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleEditErrorWhenNoArticleWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/edit/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));


    }

    @Test
    @Transactional
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
                .andExpect(redirectedUrl("/articles/details/" + articleEntity.getId() + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.articleRepository.count());
    }

    @Test
    @Transactional
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
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleDeleteErrorWhenNoArticleWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ARTICLE_CONTROLLER_PREFIX + "/delete/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void articleDeleteConfirm() throws Exception {
        long count = this.articleRepository.count();
        ArticleEntity articleEntity = this.articleRepository.findAll().get(0);
        this.mockMvc.perform(post(ARTICLE_CONTROLLER_PREFIX + "/delete/{id}", articleEntity.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/all" + FAV_ICON_EXTENSION));
        long afterDeleteCount = this.articleRepository.count();
        Assertions.assertEquals(afterDeleteCount, count-1);
    }


}