package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class UserEntity extends BaseEntity{


  private String email;
  private String fullName;
  private String password;
  private String imgUrl;
  private Set<UserRoleEntity> roles = new HashSet<>();
  private List<Cocktail> myCocktails = new ArrayList<>();


  @Column(name = "email", nullable = false, unique = true, updatable = false)
  @Pattern(regexp="^[A-Za-z0-9._%+-]+@[A-Za-z]+\\.[A-Za-z]{2,4}$")
  public String getEmail() {
    return email;
  }

  public UserEntity setEmail(String email) {
    this.email = email;
    return this;
  }


  @Column(name = "full_name", nullable = false)
  public String getFullName() {
    return fullName;
  }

  public UserEntity setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }


  @Column(name = "password", nullable = false)
  public String getPassword() {
    return password;
  }

  public UserEntity setPassword(String password) {
    this.password = password;
    return this;
  }

  @Column(name = "img_url")
  public String getImgUrl() {
    return imgUrl;
  }

  public UserEntity setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
    return this;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  public Set<UserRoleEntity> getRoles() {
    return roles;
  }

  public UserEntity setRoles(Set<UserRoleEntity> roles) {
    this.roles = roles;
    return this;
  }

  @OneToMany
  public List<Cocktail> getMyCocktails() {
    return myCocktails;
  }

  public UserEntity setMyCocktails(List<Cocktail> myCocktails) {
    this.myCocktails = myCocktails;
    return this;
  }

  public UserEntity addRole(UserRoleEntity roleEntity) {
    this.roles.add(roleEntity);
    return this;
  }
}
