package bg.softuni.needadrink.domain.entities;



import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cocktails")
public class Cocktail extends BaseEntity {
    private String name;
    private String description;
    private String imgUrl;
    private int likes;
    private List<Ingredient> ingredients;

    public Cocktail() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public Cocktail setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public Cocktail setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public Cocktail setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @Column(name = "likes", nullable = false)
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
}
