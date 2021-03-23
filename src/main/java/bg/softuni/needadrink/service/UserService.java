package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;

public interface UserService {
    boolean emailExists(String email);

    void registerAndLoginUser(UserRegisterServiceModel registerServiceModel);

    void initAdminUser();

    UserServiceModel findUserByEmail(String email);

    UserServiceModel editUserProfile(UserServiceModel serviceModel, String oldPassword);
}
