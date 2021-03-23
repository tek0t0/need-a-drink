package bg.softuni.needadrink.domain.models.views;


import bg.softuni.needadrink.domain.models.service.RoleServiceModel;

import java.time.LocalDate;
import java.util.Set;

public class UserListViewModel {
    private String id;
    private String email;
    private String fullName;
    private LocalDate birthDate;
    private Set<RoleServiceModel> roles;


    public String getId() {
        return id;
    }

    public UserListViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserListViewModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserListViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserListViewModel setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Set<RoleServiceModel> getRoles() {
        return roles;
    }

    public UserListViewModel setRoles(Set<RoleServiceModel> roles) {
        this.roles = roles;
        return this;
    }
}
