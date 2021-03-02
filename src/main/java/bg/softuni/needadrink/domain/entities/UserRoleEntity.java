package bg.softuni.needadrink.domain.entities;


import bg.softuni.needadrink.domain.entities.enums.UserRoleEnum;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {

    private UserRoleEnum name;

    public UserRoleEntity() {
    }

    public UserRoleEntity(UserRoleEnum name) {
        this.name = name;
    }



    @Enumerated(EnumType.STRING)
    public UserRoleEnum getName() {
        return name;
    }

    public UserRoleEntity setName(UserRoleEnum role) {
        this.name = role;
        return this;
    }
}
