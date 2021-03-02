package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class UserEntity extends BaseEntity{


  private String name;
  private String password;
  private List<UserRoleEntity> roles = new ArrayList<>();
  private List<Cocktail> favoriteCocktails = new ArrayList<>();
  private List<Cocktail> myCocktails = new ArrayList<>();
  private Barshelf barShelf;



  public String getName() {
    return name;
  }

  @Column(nullable = false)
  public UserEntity setName(String name) {
    this.name = name;
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
  public List<UserRoleEntity> getRoles() {
    return roles;
  }

  public UserEntity setRoles(List<UserRoleEntity> roles) {
    this.roles = roles;
    return this;
  }

  @ManyToMany
  public List<Cocktail> getFavoriteCocktails() {
    return favoriteCocktails;
  }

  public UserEntity setFavoriteCocktails(List<Cocktail> favoriteCocktails) {
    this.favoriteCocktails = favoriteCocktails;
    return this;
  }

  @OneToOne
  public Barshelf getBarShelf() {
    return barShelf;
  }

  public UserEntity setBarShelf(Barshelf barshelf) {
    this.barShelf = barshelf;
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
