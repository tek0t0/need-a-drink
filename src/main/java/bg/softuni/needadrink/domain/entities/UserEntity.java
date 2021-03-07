package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
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
  private Set<UserRoleEntity> roles = new HashSet<>();
  private List<Cocktail> myCocktails = new ArrayList<>();


  public String getEmail() {
    return email;
  }

  public UserEntity setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public UserEntity setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  @Column(nullable = false)
  public String getPassword() {
    return password;
  }

  public UserEntity setPassword(String password) {
    this.password = password;
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
}
