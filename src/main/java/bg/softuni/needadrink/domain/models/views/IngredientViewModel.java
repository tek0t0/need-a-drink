package bg.softuni.needadrink.domain.models.views;

public class IngredientViewModel {

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public IngredientViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IngredientViewModel setDescription(String description) {
        this.description = description;
        return this;
    }
}
