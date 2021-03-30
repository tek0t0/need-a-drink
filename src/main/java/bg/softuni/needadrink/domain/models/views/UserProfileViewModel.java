package bg.softuni.needadrink.domain.models.views;

import java.time.LocalDate;

public class UserProfileViewModel {


    private String email;

    private String fullName;

    private LocalDate birthDate;

    private String imgUrl;

    public String getEmail() {
        return email;
    }

    public UserProfileViewModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserProfileViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserProfileViewModel setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public UserProfileViewModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
