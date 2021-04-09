package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.repositiry.LogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class LogServiceImplTest {

    private LogServiceImpl serviceToTest;

    @Mock
    private LogServiceModel testLogServiceEntity;

    @Mock
    private LogRepository mockLogRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {

        testLogServiceEntity = new LogServiceModel()
                .setUsername("AA")
                .setTime(LocalDateTime.now())
                .setId("A");

        serviceToTest = new LogServiceImpl(
                mockLogRepository,
                new ModelMapper()
        );
    }

    @AfterEach
    public void reset() {
        Mockito.reset(mockLogRepository);
    }

    @Test
    void testSeedLogInDB() {
        serviceToTest.seedLogInDB(testLogServiceEntity);
    }


}
