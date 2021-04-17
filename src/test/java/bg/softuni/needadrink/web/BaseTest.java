package bg.softuni.needadrink.web;

import bg.softuni.needadrink.repositiry.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BaseTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ArticleRepository articleRepository;
    @Autowired
    protected IngredientRepository ingredientRepository;
    @Autowired
    protected LogRepository logRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CocktailRepository cocktailRepository;
    @Autowired
    protected UserRoleRepository roleRepository;
    @Autowired
    protected CommentRepository commentRepository;

    private String testArticleId;
    private String testCommentId;
    private String testUserId;
    private String testCocktailId;
    private String testIngredientId;
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





}
