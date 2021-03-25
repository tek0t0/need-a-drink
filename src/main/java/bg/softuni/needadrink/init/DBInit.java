package bg.softuni.needadrink.init;


import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.service.ArticleService;
import bg.softuni.needadrink.service.IngredientService;
import bg.softuni.needadrink.service.UserRoleEntityService;
import bg.softuni.needadrink.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements CommandLineRunner {

    private final UserRoleEntityService userRoleEntityService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ArticleService articleService;
    private final IngredientService ingredientService;
    private final IngredientRepository ingredientRepository;

    public DBInit(UserRoleEntityService userRoleEntityService, UserRepository userRepository, UserService userService, UserRepository userRepository1, ArticleService articleService, IngredientService ingredientService, IngredientRepository ingredientRepository) {
        this.userRoleEntityService = userRoleEntityService;
        this.userService = userService;
        this.userRepository = userRepository1;
        this.articleService = articleService;
        this.ingredientService = ingredientService;
        this.ingredientRepository = ingredientRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        userRoleEntityService.initRoles();

        if (userRepository.count() == 0) {
            userService.initAdminUser();
        }

        articleService.initArticles();

        if (ingredientRepository.count() == 0){
            ingredientService.seedIngredients();
        }

    }
}
