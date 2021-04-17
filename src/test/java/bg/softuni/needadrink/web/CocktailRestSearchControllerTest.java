package bg.softuni.needadrink.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CocktailRestSearchControllerTest extends BaseTest{

    private String testArticleId;
    private String testCommentId;
    private String testUserId;
    private String testCocktailId;
    private String testIngredientId;

//    private static final String FAV_ICON_EXTENSION = "?favicon=%2Fimages%2Ffav_icon.png";

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
