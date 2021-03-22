package bg.softuni.needadrink.domain.models.service;

import java.time.LocalDate;

public class UserServiceModel {
    private String email;
    private String fullName;
    private String imgUrl;
    private LocalDate birthDate;
    private String password;


    public String getEmail() {
        return email;
    }

    public UserServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserServiceModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public UserServiceModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserServiceModel setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }
}
