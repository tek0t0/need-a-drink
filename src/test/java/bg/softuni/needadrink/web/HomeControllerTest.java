package bg.softuni.needadrink.web;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HomeControllerTest extends BaseTest {

    private static final String TITLE_FAV_ICON_EXTENSION = "?title=Need+A+Drink%21+-+Welcome&favicon=%2Fimages%2Ffav_icon.png";

    @Test
    @Transactional
    void indexRedirectToIndexIfNoLoggedInUser() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg", password = "xyz", authorities = {"ADMIN", "USER"})
    void indexRedirectToHomeIfLoggedInUser() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home" + TITLE_FAV_ICON_EXTENSION));
    }

    @Test
    @Transactional
    @WithMockUser(username = "test@test.bg")
    void home() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cocktailSearchViewModel"))
                .andExpect(view().name("home"));
    }

}
