package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface UserService {
    boolean emailExists(String email);

    void registerAndLoginUser(UserRegisterServiceModel registerServiceModel);

    void initAdminUser();

    UserServiceModel findUserByEmail(String email);

    UserServiceModel editUserProfile(UserServiceModel serviceModel);

    List<UserServiceModel> findAllUsers();

    void setAsAdmin(String id);

    void setAsUser(String id);

    void deleteUser(String id);

    UserServiceModel findUserById(String id);

    void addCocktailToUserFavorites(String name, String id) throws UserPrincipalNotFoundException;

    void removeCocktailToUserFavorites(String email, String id);

    boolean cocktailIsInFavorites(String id, String email);


    boolean passwordMissMatch(String email, String oldPassword);
}

