package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.entities.UserEntity;
import bg.softuni.needadrink.domain.entities.UserRoleEntity;
import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;
import bg.softuni.needadrink.repositiry.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class NADUserServiceTest {

    @Mock
    private  UserRepository mockUserRepository;

    private NeedADrinkUserService serviceToTest;

    private UserEntity testUserEntity;

    @BeforeEach
    public void init(){
        UserRoleEntity roleUser = new UserRoleEntity();
        roleUser.setName(UserRoleEnum.USER);
        UserRoleEntity roleAdmin = new UserRoleEntity();
        roleAdmin.setName(UserRoleEnum.ADMIN);

       testUserEntity = new UserEntity()
               .setEmail("A")
               .setImgUrl("AA")
               .setBirthDate(LocalDate.now())
               .setPassword("AAA")
               .setFullName("AAA AAA")
               .setFavoriteCocktails(List.of())
               .setRoles(Set.of(roleUser, roleAdmin));


        serviceToTest = new NeedADrinkUserService(mockUserRepository);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername(){
        Mockito.when(mockUserRepository.findByEmail("A")).thenReturn(Optional.of(testUserEntity));

        UserDetails userDetails = serviceToTest.loadUserByUsername("A");

        Assertions.assertEquals(testUserEntity.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(2, userDetails.getAuthorities().size());

        List<String> authorities = userDetails.
                getAuthorities().
                stream().
                map(GrantedAuthority::getAuthority).
                collect(Collectors.toList());

        Assertions.assertTrue(authorities.contains("ROLE_ADMIN"));
        Assertions.assertTrue(authorities.contains("ROLE_USER"));
    }


}
