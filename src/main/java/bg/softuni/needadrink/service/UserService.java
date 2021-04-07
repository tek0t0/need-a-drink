package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.binding.UserEditBindingModel;
import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;

import java.io.IOException;
import java.util.List;

public interface UserService{

    boolean emailExists(String email);

    void registerAndLoginUser(UserRegisterServiceModel registerServiceModel);

    void initAdminUser();

    UserServiceModel findUserByEmail(String email);

    void editUserProfile(UserEditBindingModel userEditBindingModel) throws IOException;

    List<UserServiceModel> findAllUsers();

    void setAsAdmin(String id);

    void setAsUser(String id);

    void deleteUser(String id);

    UserServiceModel findUserById(String id);

    void addCocktailToUserFavorites(String name, String id);

    void removeCocktailToUserFavorites(String email, String id);

    boolean cocktailIsInFavorites(String id, String email);

    boolean passwordMissMatch(String email, String oldPassword);
}

