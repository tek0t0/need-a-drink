package bg.softuni.needadrink.service.impl;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.error.Constants;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.repositiry.UserRoleRepository;
import bg.softuni.needadrink.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final NeedADrinkUserService needADrinkUserService;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository, NeedADrinkUserService needADrinkUserService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.needADrinkUserService = needADrinkUserService;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void registerAndLoginUser(UserRegisterServiceModel registerServiceModel) {
        UserEntity newUser = modelMapper.map(registerServiceModel, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(registerServiceModel.getPassword()));

        UserRoleEntity userRole =  userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(
                () -> new IllegalStateException("USER role not found. Please seed the roles."));

        newUser.addRole(userRole);
        newUser.setImgUrl("static/images/default-user-img.jpg");

        newUser = userRepository.save(newUser);

        UserDetails principal = needADrinkUserService.loadUserByUsername(newUser.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                newUser.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void initAdminUser() {
        //TODO handle exceptions
        UserRoleEntity userRole = userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow();
        UserRoleEntity adminRole = userRoleRepository.findByName(UserRoleEnum.ADMIN).orElseThrow();

        UserEntity adminUser = new UserEntity();
        adminUser
                .addRole(userRole)
                .addRole(adminRole)
                .setEmail("admin@abv.bg")
                .setFullName("admin adminov")
                .setPassword(passwordEncoder.encode("12345"))
                .setBirthDate(LocalDate.of(1983,11,5))
                .setImgUrl(Constants.DEFAULT_USER_IMG_URL)
                .setMyCocktails(new ArrayList<>());
        this.userRepository.saveAndFlush(adminUser);
    }

    @Override
    public UserServiceModel findUserByEmail(String email) {
        UserEntity user = this.userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(Constants.EMAIL_NOT_FOUND));
        return modelMapper.map(user, UserServiceModel.class);
    }
}
