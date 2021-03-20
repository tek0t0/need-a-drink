package bg.softuni.needadrink.domain.models.binding;

import bg.softuni.needadrink.domain.validators.FieldMatch;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@FieldMatch(first = "password", second = "confirmPassword")
public class UserRegisterBindingModel {
    private String email;
    private String fullName;
    private String password;
    private LocalDate birthDate;
    private String confirmPassword;

    @NotBlank
    @Pattern(regexp = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})", message = "Invalid email!")
    public String getEmail() {
        return email;
    }

    public UserRegisterBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    @NotBlank
    @Size(min = 5, max = 30, message = "Full Name must be between 5 and 30 characters!")
    public String getFullName() {
        return fullName;
    }

    public UserRegisterBindingModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserRegisterBindingModel setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    @NotBlank
    @Size(min = 5, message = "Password must be 5 or more symbols!")
    public String getPassword() {
        return password;
    }

    public UserRegisterBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    @NotBlank
    @Size(min = 5)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
