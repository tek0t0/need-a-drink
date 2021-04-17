package bg.softuni.needadrink.web;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CocktailRestSearchControllerTest extends BaseTest{


    @Test
    @Transactional
    @WithMockUser(value = "test@test.bg", roles = {"USER", "ADMIN"})
    void testFetchAlbums() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/cocktails/search")).
                andExpect(status().isOk()).
                andExpect(jsonPath("[0].name").value("testName")).
                andExpect(jsonPath("[0].description").value("testDescription")).
                andExpect(jsonPath("[0].imgUrl").value("testImgUrl.png"));
    }
}
