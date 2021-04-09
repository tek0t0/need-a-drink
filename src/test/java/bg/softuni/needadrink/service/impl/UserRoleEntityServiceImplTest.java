package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.repositiry.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserRoleEntityServiceImplTest {

    private UserRoleEntityServiceImpl serviceToTest;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @BeforeEach
    void init(){
        serviceToTest = new UserRoleEntityServiceImpl(mockUserRoleRepository);
    }

    @Test
    void testSeedRoles(){
        Mockito.when(mockUserRoleRepository.count()).thenReturn(2L);

        serviceToTest.initRoles();
    }

}
