package bg.softuni.needadrink.domain.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity{
    private String name;
    private List<Cocktail> usedIn;

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    @ManyToMany(mappedBy = "ingredients")
    public List<Cocktail> getUsedIn() {
        return usedIn;
    }

    public Ingredient setUsedIn(List<Cocktail> usedIn) {
        this.usedIn = usedIn;
        return this;
    }
}
