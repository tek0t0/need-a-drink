package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity{
    private String name;
    private String imgUrl;
    private List<Cocktail> usedIn;

    public Ingredient() {
        this.usedIn = new ArrayList<>();
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public Ingredient setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL)
    public List<Cocktail> getUsedIn() {
        return usedIn;
    }

    public Ingredient setUsedIn(List<Cocktail> usedIn) {
        this.usedIn = usedIn;
        return this;
    }
}
