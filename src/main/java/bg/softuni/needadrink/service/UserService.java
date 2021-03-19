package bg.softuni.needadrink.service;

import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;

public interface UserService {
    boolean emailExists(String email);

    void registerAndLoginUser(UserRegisterServiceModel registerServiceModel);

    void initAdminUser();
}
