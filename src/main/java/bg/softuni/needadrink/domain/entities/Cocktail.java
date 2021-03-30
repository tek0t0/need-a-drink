package bg.softuni.needadrink.domain.entities;



import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cocktails")
public class Cocktail extends BaseEntity {

    private String name;

    private String description;

    private String preparation;

    private String imgUrl;

    private Set<Ingredient> ingredients;

    public Cocktail() {
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public Cocktail setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "description", nullable = false,columnDefinition = "TEXT" )
    public String getDescription() {
        return description;
    }

    public Cocktail setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "preparation",columnDefinition = "TEXT")
    public String getPreparation() {
        return preparation;
    }

    public Cocktail setPreparation(String preparation) {
        this.preparation = preparation;
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

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Cocktail setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
}
