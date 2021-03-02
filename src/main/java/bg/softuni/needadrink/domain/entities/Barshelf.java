package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bar_shelfs")
public class Barshelf extends BaseEntity{
    private UserEntity owner;
    private List<Ingredient> ingredients;

    public Barshelf() {
    }

    @OneToOne
    public UserEntity getOwner() {
        return owner;
    }

    public Barshelf setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }

    @ManyToMany
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Barshelf setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}

