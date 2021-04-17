package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.domain.models.binding.UserEditBindingModel;
import bg.softuni.needadrink.domain.models.service.UserServiceModel;
import bg.softuni.needadrink.error.CocktailNotFoundException;
import bg.softuni.needadrink.error.RoleNotFoundException;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.repositiry.UserRoleRepository;
import bg.softuni.needadrink.service.CloudinaryService;
import bg.softuni.needadrink.service.CommentService;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {


    private UserServiceImpl serviceToTest;


    private UserEntity testUser1, testUser2;


    private UserRoleEntity roleUser, roleAdmin;



    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Mock
    private NeedADrinkUserService mockNeedADrinkUserService;

    @Mock
    private CocktailRepository mockCocktailRepository;

    @Mock
    private LogService mockLogService;

    @Mock
    private CommentService mockCommentService;


    @Mock
    private CloudinaryService mockCloudinaryService;

    @BeforeEach
    public void init() {


        roleUser = new UserRoleEntity();
        roleUser.setName(UserRoleEnum.USER);
        roleAdmin = new UserRoleEntity();
        roleAdmin.setName(UserRoleEnum.ADMIN);

        testUser1 = new UserEntity()
                .setEmail("A")
                .setImgUrl("AA")
                .setBirthDate(LocalDate.now())
                .setPassword("AAA")
                .setFullName("AAA AAA")
                .setFavoriteCocktails(List.of())
                .setRoles(Set.of(roleUser, roleAdmin));

        testUser2 = new UserEntity()
                .setEmail("B")
                .setImgUrl("BB")
                .setBirthDate(LocalDate.now())
                .setPassword("BBB")
                .setFullName("BBB BBB")
                .setFavoriteCocktails(List.of())
                .setRoles(Set.of(roleUser));


        serviceToTest = new UserServiceImpl(
                mockUserRepository,
                new ModelMapper(),
                passwordEncoder,
                mockUserRoleRepository,
                mockNeedADrinkUserService,
                mockCocktailRepository,
                mockLogService,
                mockCloudinaryService,
                mockCommentService);

    }

    @AfterEach
    public void reset() {
        Mockito.reset(mockUserRepository);
    }

    @Test
    void testEmailExists() {
        Mockito.when(mockUserRepository.findByEmail("AAA")).thenReturn(Optional.ofNullable(testUser1));

        Assertions.assertTrue(this.serviceToTest.emailExists("AAA"));
    }

    @Test
    void testInitAdminThrowsException() {
        Assertions.assertThrows(RoleNotFoundException.class, () -> serviceToTest.initAdminUser());

    }

    @Test
    void testInitAdmin() {
        UserRoleEntity user = new UserRoleEntity();
        user.setName(UserRoleEnum.USER);
        user.setId("2");

        UserRoleEntity admin = new UserRoleEntity();
        admin.setName(UserRoleEnum.ADMIN);
        admin.setId("1");

        Mockito.when(mockUserRoleRepository.findByName(UserRoleEnum.ADMIN)).thenReturn(Optional.of(admin));
        Mockito.when(mockUserRoleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode("secret")).thenReturn("secret");

        UserEntity adminUser = new UserEntity()
                .addRole(admin)
                .setEmail("admin@abv.bg")
                .setFullName("admin adminov")
                .setPassword(passwordEncoder.encode("secret"))
                .setBirthDate(LocalDate.of(1983, 11, 5))
                .setImgUrl(Constants.DEFAULT_USER_IMG_URL)
                .setFavoriteCocktails(new ArrayList<>());
        serviceToTest.initAdminUser();
    }

    @Test
    void testFindUserByEmailThrowsException() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.findUserByEmail("A"));
    }

    @Test
    void testFindAllUsers(){
        Mockito.when(this.mockUserRepository.findAll()).thenReturn(List.of(testUser1,testUser2));

        List<UserServiceModel> allUsers = serviceToTest.findAllUsers();
        UserServiceModel model1 = allUsers.get(0);
        UserServiceModel model2 = allUsers.get(1);

        Assertions.assertEquals(model1.getId(),testUser1.getId());
        Assertions.assertEquals(model1.getEmail(),testUser1.getEmail());
        Assertions.assertEquals(model1.getImgUrl(),testUser1.getImgUrl());
        Assertions.assertEquals(model1.getFullName(),testUser1.getFullName());

        Assertions.assertEquals(model2.getId(),testUser2.getId());
        Assertions.assertEquals(model2.getEmail(),testUser2.getEmail());
        Assertions.assertEquals(model2.getImgUrl(),testUser2.getImgUrl());
        Assertions.assertEquals(model2.getFullName(),testUser2.getFullName());
    }

    @Test
    void testEditUser() throws IOException {

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        UserEditBindingModel model = new UserEditBindingModel()
                .setBirthDate(LocalDate.now())
                .setEmail("A")
                .setFullName("full name")
                .setPassword("testPassword")
                .setImg(file);

        Mockito.when(this.mockUserRepository.findByEmail("A")).thenReturn(Optional.of(testUser1));
        Mockito.when(this.passwordEncoder.encode("testPassword")).thenReturn("secret");
        serviceToTest.editUserProfile(model);

    }

    @Test
    void testSetAdminThrowsIfUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.setAsAdmin("ABC"));
    }

    @Test
    void testSetAdmin(){
        Mockito.when(this.mockUserRepository.findById("B")).thenReturn(Optional.of(testUser2));
        Mockito.when(this.mockUserRoleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(roleUser));
        Mockito.when(this.mockUserRoleRepository.findByName(UserRoleEnum.ADMIN)).thenReturn(Optional.of(roleAdmin));

        this.serviceToTest.setAsAdmin("B");

        ArgumentCaptor<UserEntity> argument = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(this.mockUserRepository).saveAndFlush(argument.capture());

        UserEntity userEntity = argument.getValue();

        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(2, userEntity.getRoles().size());
    }

    @Test
    void testSetUserThrowsIfUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.setAsUser("ABC"));
    }

    @Test
    void testSetUser(){

        Mockito.when(this.mockUserRepository.findById("A")).thenReturn(Optional.of(testUser1));
        Mockito.when(this.mockUserRoleRepository.findByName(UserRoleEnum.USER)).thenReturn(Optional.of(roleUser));

        this.serviceToTest.setAsUser("A");

        ArgumentCaptor<UserEntity> argument = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(this.mockUserRepository).saveAndFlush(argument.capture());

        UserEntity userEntity = argument.getValue();

        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(1, userEntity.getRoles().size());
    }

    @Test
    void testDeleteUserThrowsIfUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.deleteUser("ABC"));
    }

    @Test
    void testDeleteUser(){
        Mockito.when(this.mockUserRepository.findById("A")).thenReturn(Optional.of(testUser1));

        serviceToTest.deleteUser("A");

        ArgumentCaptor<UserEntity> argument = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(this.mockUserRepository).delete(argument.capture());
    }

    @Test
    void testFindUserByIdThrowsIfUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.findUserById("ABC"));
    }

    @Test
    void testFindUserById(){
        Mockito.when(this.mockUserRepository.findById("A")).thenReturn(Optional.of(testUser1));

        UserServiceModel model = serviceToTest.findUserById("A");

        Assertions.assertEquals(model.getFullName(),testUser1.getFullName());
        Assertions.assertEquals(model.getId(),testUser1.getId());
        Assertions.assertEquals(model.getEmail(),testUser1.getEmail());
        Assertions.assertEquals(model.getBirthDate(),testUser1.getBirthDate());
        Assertions.assertEquals(model.getImgUrl(),testUser1.getImgUrl());
    }

    @Test
    void testAddCocktailToUserFavoritesThrowsIfUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.addCocktailToUserFavorites("testName","ABC"));
    }

    @Test
    void testAddCocktailToUserFavoritesThrowsIfCocktailNotFound(){
        Mockito.when(this.mockUserRepository.findByEmail("AAA")).thenReturn(Optional.of(testUser1));
        Assertions.assertThrows(CocktailNotFoundException.class, () -> serviceToTest.addCocktailToUserFavorites("AAA","ABC"));
    }
}
