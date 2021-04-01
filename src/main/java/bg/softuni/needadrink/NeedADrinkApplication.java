package bg.softuni.needadrink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NeedADrinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeedADrinkApplication.class, args);
    }

}
