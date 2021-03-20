package bg.softuni.needadrink.init;


import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.service.ArticleService;
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

    public DBInit(UserRoleEntityService userRoleEntityService, UserRepository userRepository, UserService userService, UserRepository userRepository1, ArticleService articleService) {
        this.userRoleEntityService = userRoleEntityService;
        this.userService = userService;
        this.userRepository = userRepository1;
        this.articleService = articleService;
    }


    @Override
    public void run(String... args) throws Exception {

        userRoleEntityService.initRoles();

        if(userRepository.count() == 0) {
            userService.initAdminUser();
        }

        articleService.initArticles();


    }
}
