package bg.softuni.needadrink.init;


import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.service.UserRoleEntityService;
import bg.softuni.needadrink.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements CommandLineRunner {

    private final UserRoleEntityService userRoleEntityService;
    private final UserService userService;
    private final UserRepository userRepository;

    public DBInit(UserRoleEntityService userRoleEntityService, UserRepository userRepository, UserService userService, UserRepository userRepository1) {
        this.userRoleEntityService = userRoleEntityService;
        this.userService = userService;
        this.userRepository = userRepository1;
    }


    @Override
    public void run(String... args) throws Exception {

        userRoleEntityService.initRoles();
        if(userRepository.count() == 0) {
            userService.initAdminUser();
        }


    }
}
