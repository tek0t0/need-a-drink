package bg.softuni.needadrink.init;


import bg.softuni.needadrink.service.UserRoleEntityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements CommandLineRunner {

  private final UserRoleEntityService userRoleEntityService;

    public DBInit(UserRoleEntityService userRoleEntityService) {
        this.userRoleEntityService = userRoleEntityService;
    }


    @Override
    public void run(String... args) throws Exception {

    userRoleEntityService.initRoles();


    }
}
