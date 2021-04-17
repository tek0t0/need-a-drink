package bg.softuni.needadrink.web;


import bg.softuni.needadrink.domain.entities.ArticleEntity;
import bg.softuni.needadrink.domain.entities.CocktailEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CocktailControllerTest extends BaseTest {

    private String testArticleId;
    private String testCommentId;
    private String testUserId;
    private String testCocktailId;
    private String testIngredientId;

    private static final String COCKTAIL_CONTROLLER_PREFIX = "/cocktails";
    private static final String FAV_ICON_EXTENSION = "?favicon=%2Fimages%2Ffav_icon.png";

    private TestData testData;

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
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    void allCocktails() throws Exception {
        this.mockMvc.perform(get(COCKTAIL_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("cocktail/all-cocktails"))
                .andExpect(model().attributeExists("allCocktails"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void cocktailDetailsRedirectWhenNOCocktailWithThatId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(COCKTAIL_CONTROLLER_PREFIX + "/details/{id}", 1)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    public void detailsCocktail() throws Exception {
        CocktailEntity cocktailEntity = this.cocktailRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.get(COCKTAIL_CONTROLLER_PREFIX + "/details/{id}", cocktailEntity.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cocktailViewModel"))
                .andExpect(model().attributeExists("ingredients"))
                .andExpect(model().attributeExists("alreadyInFavorite"))
                .andExpect(view().name("cocktail/details-cocktail"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addCocktailRedirectsAndDoesNOtAddCocktailToTheRepoWhenThereAreMistakesInBinding() throws Exception {
        long count = this.cocktailRepository.count();
        this.mockMvc.perform(post(COCKTAIL_CONTROLLER_PREFIX + "/add")
                .param("name", "1")
                .param("description", "1")
                .param("preparation", "1")
                .param("imgUrl", "1")
                .param("videoUrl", "1")
                .with(csrf()))
                .andExpect(flash().attributeExists("cocktailInitBindingModel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(COCKTAIL_CONTROLLER_PREFIX + "/add" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.cocktailRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addCocktailRedirectsAndDoesNOtAddCocktailToTheRepoWhenNameExists() throws Exception {
        long count = this.cocktailRepository.count();
        this.mockMvc.perform(post(COCKTAIL_CONTROLLER_PREFIX + "/add")
                .param("name", "testName")
                .param("description", "someDescriptionHere")
                .param("preparation", "somePreparationHere")
                .param("imgUrl", "imgUrl.png")
                .param("videoUrl", "1asdsa")
                .with(csrf()))
                .andExpect(flash().attributeExists("cocktailInitBindingModel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(COCKTAIL_CONTROLLER_PREFIX + "/add" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.cocktailRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addCocktail() throws Exception {
        long count = this.cocktailRepository.count();
        this.mockMvc.perform(post(COCKTAIL_CONTROLLER_PREFIX + "/add")
                .param("name", "newTestname")
                .param("description", "someDescriptionHere")
                .param("preparation", "somePreparationHere")
                .param("imgUrl", "imgUrl.png")
                .param("videoUrl", "1asdsa")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(COCKTAIL_CONTROLLER_PREFIX + "/all" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count + 1, this.cocktailRepository.count());
    }




}
