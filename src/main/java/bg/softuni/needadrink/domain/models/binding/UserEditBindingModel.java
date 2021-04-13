package bg.softuni.needadrink.domain.models.binding;


import bg.softuni.needadrink.domain.validators.FieldMatch;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;


@FieldMatch(first = "password", second = "confirmPassword")
public class UserEditBindingModel {

    private String email;

    private String fullName;

    private LocalDate birthDate;

    private String oldPassword;

    private String password;

    private String confirmPassword;

    private MultipartFile img;


    public String getEmail() {
        return email;
    }

    public UserEditBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    @NotBlank
    @Size(min = 5, max = 30, message = "Full Name must be between 5 and 30 characters!")
    public String getFullName() {
        return fullName;
    }

    public UserEditBindingModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserEditBindingModel setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    @NotBlank
    @Size(min = 5, message = "Password must be 5 or more symbols!")
    public String getOldPassword() {
        return oldPassword;
    }

    public UserEditBindingModel setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    @Pattern(regexp ="^(|[\\w\\W\\d\\s]{4,20})$",message = "New Password must be 5 or more symbols!" )
    public String getPassword() {
        return password;
    }

    public UserEditBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserEditBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public MultipartFile getImg() {
        return img;
    }

    public UserEditBindingModel setImg(MultipartFile img) {
        this.img = img;
        return this;
    }
}
