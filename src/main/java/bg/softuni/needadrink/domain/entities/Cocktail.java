package bg.softuni.needadrink.domain.entities;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cocktails")
public class Cocktail extends BaseEntity {
    private String name;
    private String description;
    private String imgUrl;
    private String videoUrl;
    private int likes;
    private List<Ingredient> ingredients;
    private List<UserEntity> likers;

    public Cocktail() {
    }

    public String getName() {
        return name;
    }

    public Cocktail setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Cocktail setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Cocktail setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Cocktail setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public int getLikes() {
        return likes;
    }

    public Cocktail setLikes(int likes) {
        this.likes = likes;
        return this;
    }

    @ManyToMany
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Cocktail setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    @ManyToMany
    public List<UserEntity> getLikers() {
        return likers;
    }

    public Cocktail setLikers(List<UserEntity> likers) {
        this.likers = likers;
        return this;
    }
}
