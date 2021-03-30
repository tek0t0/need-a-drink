package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.error.Constants;
import bg.softuni.needadrink.error.RoleNotFoundException;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.repositiry.UserRoleRepository;
import bg.softuni.needadrink.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final NeedADrinkUserService needADrinkUserService;
    private final CocktailRepository cocktailRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository, NeedADrinkUserService needADrinkUserService, CocktailRepository cocktailRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.needADrinkUserService = needADrinkUserService;
        this.cocktailRepository = cocktailRepository;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void registerAndLoginUser(UserRegisterServiceModel registerServiceModel) {
        UserEntity newUser = modelMapper.map(registerServiceModel, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(registerServiceModel.getPassword()));

        UserRoleEntity userRole = userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(
                () -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND));

        newUser.addRole(userRole);
        //TODO: upload default img in cloud
        newUser.setImgUrl("/images/default-user-img.jpg");

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
        UserRoleEntity userRole = userRoleRepository.findByName(UserRoleEnum.USER)
                .orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND));
        UserRoleEntity adminRole = userRoleRepository.findByName(UserRoleEnum.ADMIN)
                .orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND));

        UserEntity adminUser = new UserEntity();
        adminUser
                .addRole(userRole)
                .addRole(adminRole)
                .setEmail("admin@abv.bg")
                .setFullName("admin adminov")
                .setPassword(passwordEncoder.encode("12345"))
                .setBirthDate(LocalDate.of(1983, 11, 5))
                .setImgUrl(Constants.DEFAULT_USER_IMG_URL)
                .setFavoriteCocktails(new ArrayList<>());
        this.userRepository.saveAndFlush(adminUser);
    }

    @Override
    public UserServiceModel findUserByEmail(String email) {
        UserEntity user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(Constants.EMAIL_NOT_FOUND));
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel serviceModel, String oldPassword) {
        UserEntity user = this.userRepository.findByEmail(serviceModel.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException(Constants.PASSWORD_IS_INCORRECT);
        }


        user
                .setPassword(serviceModel.getPassword())
                .setFullName(serviceModel.getFullName())
                .setImgUrl(serviceModel.getImgUrl())
                .setBirthDate(serviceModel.getBirthDate());


        //TODO:logger


        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void setAsAdmin(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));
        userEntity.getRoles().clear();
        userEntity.getRoles().add(userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND)));
        userEntity.getRoles().add(userRoleRepository.findByName(UserRoleEnum.ADMIN).orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND)));

        //TODO: Logger
        this.userRepository.saveAndFlush(userEntity);

    }

    @Override
    public void setAsUser(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));
        userEntity.getRoles().clear();
        userEntity.getRoles().add(userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND)));

        //TODO: Logger
        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));
        this.userRepository.delete(userEntity);
    }

    @Override
    public UserServiceModel findUserById(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow((() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND)));
        return modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public void addCocktailToUserFavorites(String name, String id) {
        UserEntity userEntity = this.userRepository.findByEmail(name).get();

        Cocktail cocktail = this.cocktailRepository.findById(id).get();
        List<Cocktail> favoriteCocktails = userEntity.getFavoriteCocktails();
        favoriteCocktails.add(cocktail);
        userEntity.setFavoriteCocktails(favoriteCocktails);
        this.userRepository.saveAndFlush(userEntity);

    }

    @Override
    public void removeCocktailToUserFavorites(String name, String id) {
        UserEntity userEntity = this.userRepository.findByEmail(name).get();

        Cocktail cocktail = this.cocktailRepository.findById(id).get();
        List<Cocktail> favoriteCocktails = userEntity.getFavoriteCocktails();
        favoriteCocktails.remove(cocktail);
        userEntity.setFavoriteCocktails(favoriteCocktails);
        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public boolean cocktailIsInFavorites(String id, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).get();
        return userEntity.getFavoriteCocktails().contains(this.cocktailRepository.findById(id).get());

    }
}
