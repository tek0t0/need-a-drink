package bg.softuni.needadrink.web;

import bg.softuni.needadrink.repositiry.*;
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




}
