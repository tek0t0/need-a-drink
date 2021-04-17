package bg.softuni.needadrink.web;

import bg.softuni.needadrink.domain.entities.ArticleEntity;
import bg.softuni.needadrink.domain.entities.IngredientEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest extends BaseTest {

    private static final String INGREDIENTS_CONTROLLER_PREFIX = "/ingredients";
    private static final String FAV_ICON_EXTENSION = "?favicon=%2Fimages%2Ffav_icon.png";

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    void allIngredients() throws Exception {
        this.mockMvc.perform(get(INGREDIENTS_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredient/all-ingredients"))
                .andExpect(model().attributeExists("ingredients"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", roles = {"ADMIN", "USER"})
    public void addIngredientPage() throws Exception {
        this.mockMvc.perform(get(INGREDIENTS_CONTROLLER_PREFIX + "/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredient/add-ingredient"))
                .andExpect(model().attributeExists("ingredients"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addIngredientRedirectsAndDoesNOtAddIngredientToTheRepoWhenThereAreMistakesInBinding() throws Exception {
        long count = this.ingredientRepository.count();
        this.mockMvc.perform(post(INGREDIENTS_CONTROLLER_PREFIX + "/add")
                .param("name", "1")
                .param("description", "1")
                .param("imgUrl", "1")
                .with(csrf()))
                .andExpect(flash().attributeExists("ingredientBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.ingredientBindingModel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(INGREDIENTS_CONTROLLER_PREFIX + "/add" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.ingredientRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addIngredientRedirectsAndDoesNOtAddIngredientToTheRepoWhenThereIsIngredientWithThisNAme() throws Exception {
        long count = this.ingredientRepository.count();
        this.mockMvc.perform(post(INGREDIENTS_CONTROLLER_PREFIX + "/add")
                .param("name", "testNama")
                .param("description", "asdasdasdasd")
                .param("imgUrl", "asdasdasdas.png")
                .with(csrf()))
                .andExpect(flash().attributeExists("ingredientBindingModel"))
                .andExpect(flash().attributeExists("nameExists"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(INGREDIENTS_CONTROLLER_PREFIX + "/add" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.ingredientRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void addIngredientSetDefaultImg() throws Exception {
        long count = this.ingredientRepository.count();
        this.mockMvc.perform(post(INGREDIENTS_CONTROLLER_PREFIX + "/add")
                .param("name", "newIngredient")
                .param("description", "asdasdasdasd")
                .param("imgUrl", "")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(INGREDIENTS_CONTROLLER_PREFIX + "/all" + FAV_ICON_EXTENSION));
        Assertions.assertEquals(count + 1, this.ingredientRepository.count());
        IngredientEntity newIngredient = this.ingredientRepository.getByName("newIngredient").orElseThrow();
        Assertions.assertEquals(newIngredient.getName(), "newIngredient");
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void ingredientEdit() throws Exception {
        IngredientEntity ingredientEntity = this.ingredientRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.get(INGREDIENTS_CONTROLLER_PREFIX + "/edit/{id}", ingredientEntity.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredientBindingModel"))
                .andExpect(model().attributeExists("ingredientId"))
                .andExpect(model().attributeExists("nameExists"))
                .andExpect(view().name("ingredient/edit-ingredient"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void postEditRedirectsAndDoesNOtAddIngredientToTheRepoWhenThereAreMistakesInBinding() throws Exception {
        long count = this.ingredientRepository.count();
        IngredientEntity ingredientEntity = this.ingredientRepository.findAll().get(0);
        this.mockMvc.perform(post(INGREDIENTS_CONTROLLER_PREFIX + "/edit/{id}",ingredientEntity.getId())
                .param("name", "1")
                .param("description", "1")
                .param("imgUrl", "1")
                .with(csrf()))
                .andExpect(model().attributeExists("ingredientBindingModel"))
                .andExpect(model().attributeExists("org.springframework.validation.BindingResult.ingredientBindingModel"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredient/edit-ingredient"));
        Assertions.assertEquals(count, this.ingredientRepository.count());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    public void ingredientEditConfirm() throws Exception {
        long count = this.ingredientRepository.count();
        IngredientEntity ingredientEntity = this.ingredientRepository.findAll().get(0);
        this.mockMvc.perform(post(INGREDIENTS_CONTROLLER_PREFIX + "/edit/{id}",ingredientEntity.getId())
                .param("name", "testName")
                .param("description", "sadasdasdasdas")
                .param("imgUrl", "asdasdasdasdasdasdasd.png")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(INGREDIENTS_CONTROLLER_PREFIX +"/all"+FAV_ICON_EXTENSION));
        Assertions.assertEquals(count, this.ingredientRepository.count());
        IngredientEntity newTestEntity = this.ingredientRepository.getByName("testName").orElseThrow();
        Assertions.assertEquals(newTestEntity.getDescription(), ingredientEntity.getDescription());
        Assertions.assertEquals(newTestEntity.getImgUrl(), ingredientEntity.getImgUrl());
    }

}
