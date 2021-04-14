package bg.softuni.needadrink.domain.entities;



import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cocktails")
public class CocktailEntity extends BaseEntity {

    private String name;

    private String description;

    private String preparation;

    private String imgUrl;

    private String videoUrl;

    private Set<IngredientEntity> ingredientEntities;

    public CocktailEntity() {
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public CocktailEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "description", nullable = false,columnDefinition = "TEXT" )
    public String getDescription() {
        return description;
    }

    public CocktailEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "preparation",columnDefinition = "TEXT")
    public String getPreparation() {
        return preparation;
    }

    public CocktailEntity setPreparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public CocktailEntity setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<IngredientEntity> getIngredients() {
        return ingredientEntities;
    }

    public CocktailEntity setIngredients(Set<IngredientEntity> ingredientEntities) {
        this.ingredientEntities = ingredientEntities;
        return this;
    }

    public void addIngredient(IngredientEntity ingredientEntity) {
        this.ingredientEntities.add(ingredientEntity);
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public CocktailEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
