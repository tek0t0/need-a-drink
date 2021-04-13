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
    private final CocktailService cocktailService;

    public DBInit(UserRoleEntityService userRoleEntityService,
                  UserService userService,
                  UserRepository userRepository,
                  ArticleService articleService,
                  IngredientService ingredientService,
                  CocktailService cocktailService) {
        this.userRoleEntityService = userRoleEntityService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.articleService = articleService;
        this.ingredientService = ingredientService;
        this.cocktailService = cocktailService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.userRoleEntityService.initRoles();

        if (this.userRepository.count() == 0) {
            this.userService.initAdminUser();
        }

        this.articleService.initArticles();

        this.ingredientService.seedIngredients();

        this.cocktailService.seedCocktails();


    }
}
