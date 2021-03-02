package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.repositiry.UserRoleRepository;
import bg.softuni.needadrink.service.UserRoleEntityService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleEntityServiceImpl implements UserRoleEntityService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleEntityServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void initRoles() {
        UserRoleEnum[] rolesNames = UserRoleEnum.values();
        for (UserRoleEnum role : rolesNames) {
            userRoleRepository.save(new UserRoleEntity(role));
        }
    }
}
