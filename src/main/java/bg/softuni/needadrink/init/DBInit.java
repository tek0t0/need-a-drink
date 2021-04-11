package bg.softuni.needadrink.init;


import bg.softuni.needadrink.repositiry.ArticleRepository;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.IngredientRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.service.*;
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
    private final CocktailRepository cocktailRepository;
    private final CocktailService cocktailService;
    private final ArticleRepository articleRepository;

    public DBInit(UserRoleEntityService userRoleEntityService,
                  UserService userService,
                  UserRepository userRepository,
                  ArticleService articleService,
                  IngredientService ingredientService,
                  IngredientRepository ingredientRepository,
                  CocktailRepository cocktailRepository,
                  CocktailService cocktailService,
                  ArticleRepository articleRepository) {
        this.userRoleEntityService = userRoleEntityService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.articleService = articleService;
        this.ingredientService = ingredientService;
        this.ingredientRepository = ingredientRepository;
        this.cocktailRepository = cocktailRepository;
        this.cocktailService = cocktailService;

        this.articleRepository = articleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        userRoleEntityService.initRoles();

        if (this.userRepository.count() == 0) {
            userService.initAdminUser();
        }

        articleService.initArticles();

        ingredientService.seedIngredients();

        cocktailService.seedCocktails();


    }
}
