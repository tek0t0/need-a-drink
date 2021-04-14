package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
public class IngredientEntity extends BaseEntity{

    private String name;

    private String description;

    private String imgUrl;

    private List<CocktailEntity> usedIn;

    public IngredientEntity() {
        this.usedIn = new ArrayList<>();
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public IngredientEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public IngredientEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public IngredientEntity setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL)
    public List<CocktailEntity> getUsedIn() {
        return usedIn;
    }

    public IngredientEntity setUsedIn(List<CocktailEntity> usedIn) {
        this.usedIn = usedIn;
        return this;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", usedIn=" + usedIn +
                '}';
    }
}
