package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;
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

  private LocalDate birthDate;

  private String imgUrl;

  private Set<UserRoleEntity> roles;

  private List<Cocktail> favoriteCocktails;

  public UserEntity() {
    this.roles = new HashSet<>();
    this.favoriteCocktails = new ArrayList<>();
  }

  @Column(name = "email", nullable = false, unique = true, updatable = false)
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

  @Column(name = "birth_date", nullable = false)
  public LocalDate getBirthDate() {
    return birthDate;
  }

  public UserEntity setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
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
  public List<Cocktail> getFavoriteCocktails() {
    return favoriteCocktails;
  }

  public UserEntity setFavoriteCocktails(List<Cocktail> myCocktails) {
    this.favoriteCocktails = myCocktails;
    return this;
  }

  public UserEntity addRole(UserRoleEntity roleEntity) {
    this.roles.add(roleEntity);
    return this;
  }


}
