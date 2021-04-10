package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.error.RoleNotFoundException;
import bg.softuni.needadrink.repositiry.CocktailRepository;
import bg.softuni.needadrink.repositiry.UserRepository;
import bg.softuni.needadrink.repositiry.UserRoleRepository;
import bg.softuni.needadrink.service.CloudinaryService;
import bg.softuni.needadrink.service.LogService;
import bg.softuni.needadrink.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserServiceImpl serviceToTest;

    @Mock
    private UserEntity testUser1, testUser2;

    private UserRoleEntity roleUser, roleAdmin;

    @Mock
    private ModelMapper modelMapper;

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
                .setRoles(Set.of(roleUser, roleAdmin));


        serviceToTest = new UserServiceImpl(
                mockUserRepository,
                modelMapper,
                passwordEncoder,
                mockUserRoleRepository,
                mockNeedADrinkUserService,
                mockCocktailRepository,
                mockLogService,
                mockCloudinaryService
        );

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
}
