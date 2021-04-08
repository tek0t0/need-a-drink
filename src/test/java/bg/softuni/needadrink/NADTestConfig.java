package bg.softuni.needadrink;

import bg.softuni.needadrink.service.CloudinaryService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NADTestConfig {
    @MockBean
    public CloudinaryService cloudinaryService;
}
