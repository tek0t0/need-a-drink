package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.Cocktail;
import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.domain.models.binding.UserEditBindingModel;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.domain.models.service.UserRegisterServiceModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.error.CocktailNotFoundException;
import bg.softuni.needadrink.service.CloudinaryService;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final NeedADrinkUserService needADrinkUserService;
    private final CocktailRepository cocktailRepository;
    private final LogService logService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository, NeedADrinkUserService needADrinkUserService, CocktailRepository cocktailRepository, LogService logService, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.needADrinkUserService = needADrinkUserService;
        this.cocktailRepository = cocktailRepository;
        this.logService = logService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void registerAndLoginUser(UserRegisterServiceModel registerServiceModel) {
        UserEntity userEntity = modelMapper.map(registerServiceModel, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerServiceModel.getPassword()));

        UserRoleEntity userRole = userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(
                () -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND));

        userEntity.addRole(userRole);
        //TODO: upload default img in cloud
        userEntity.setImgUrl(Constants.DEFAULT_USER_IMG_PATH);

        userEntity = userRepository.save(userEntity);

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(userEntity.getEmail());
        logServiceModel.setDescription("User registered");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        UserDetails principal = needADrinkUserService.loadUserByUsername(userEntity.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                userEntity.getPassword(),
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
    public void editUserProfile(UserEditBindingModel bindingModel) throws IOException {

        MultipartFile img = bindingModel.getImg();
        String imageUrl = cloudinaryService.uploadImage(img);


        UserEntity userEntity = this.userRepository.findByEmail(bindingModel.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        userEntity
                .setFullName(bindingModel.getFullName())
                .setPassword(passwordEncoder.encode(bindingModel.getPassword()))
                .setImgUrl(imageUrl)
                .setBirthDate(bindingModel.getBirthDate());


        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(userEntity.getEmail());
        logServiceModel.setDescription("User profile edited.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.userRepository.saveAndFlush(userEntity);
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

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(userEntity.getEmail());
        logServiceModel.setDescription("Admin role added.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.userRepository.saveAndFlush(userEntity);

    }

    @Override
    public void setAsUser(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));
        userEntity.getRoles().clear();
        userEntity.getRoles().add(userRoleRepository.findByName(UserRoleEnum.USER).orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND)));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(userEntity.getEmail());
        logServiceModel.setDescription("User role added.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(userEntity.getEmail());
        logServiceModel.setDescription("User deleted.");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        this.userRepository.delete(userEntity);
    }

    @Override
    public UserServiceModel findUserById(String id) {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow((() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND)));
        return modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public void addCocktailToUserFavorites(String name, String id) {
        UserEntity userEntity = this.userRepository.findByEmail(name).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        Cocktail cocktail = this.cocktailRepository.findById(id).orElseThrow(() -> new CocktailNotFoundException(Constants.COCKTAIL_ID_NOT_FOUND));
        List<Cocktail> favoriteCocktails = userEntity.getFavoriteCocktails();
        favoriteCocktails.add(cocktail);
        userEntity.setFavoriteCocktails(favoriteCocktails);
        this.userRepository.saveAndFlush(userEntity);

    }

    @Override
    public void removeCocktailToUserFavorites(String name, String id) {
        UserEntity userEntity = this.userRepository.findByEmail(name).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND));

        Cocktail cocktail = this.cocktailRepository.findById(id).orElseThrow(() -> new CocktailNotFoundException(Constants.COCKTAIL_ID_NOT_FOUND));
        List<Cocktail> favoriteCocktails = userEntity.getFavoriteCocktails();
        favoriteCocktails.remove(cocktail);
        userEntity.setFavoriteCocktails(favoriteCocktails);
        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public boolean cocktailIsInFavorites(String id, String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_EMAIL_NOT_FOUND));
        return userEntity
                .getFavoriteCocktails()
                .contains(this.cocktailRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Constants.USER_ID_NOT_FOUND)));

    }

    @Override
    public boolean passwordMissMatch(String email, String oldPassword) {

        UserEntity userEntity = this.userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(Constants.USER_EMAIL_NOT_FOUND));

        return !passwordEncoder.matches(oldPassword, userEntity.getPassword());
    }
}
