package bg.softuni.needadrink.web;


import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CocktailControllerTest extends BaseTest {

    private static final String COCKTAIL_CONTROLLER_PREFIX = "/cocktails";
    private static final String FAV_ICON_EXTENSION = "?favicon=%2Fimages%2Ffav_icon.png";


    @Test
    @WithMockUser(username = "admin@admin.bg", roles = {"ADMIN", "USER"})
    void allCocktails() throws Exception {
        this.mockMvc.perform(get(COCKTAIL_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("cocktail/all-cocktails"))
                .andExpect(model().attributeExists("allCocktails"));
    }
}
