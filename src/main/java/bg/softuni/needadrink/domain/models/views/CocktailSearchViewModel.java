package bg.softuni.needadrink.domain.models.views;


import java.util.List;

public class CocktailSearchViewModel {

    private String id;

    private String name;

    private String description;

    private String imgUrl;

    private List<String> ingredientsNames;


    public String getId() {
        return id;
    }

    public CocktailSearchViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CocktailSearchViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CocktailSearchViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public CocktailSearchViewModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public List<String> getIngredientsNames() {
        return ingredientsNames;
    }

    public CocktailSearchViewModel setIngredientsNames(List<String> ingredientsNames) {
        this.ingredientsNames = ingredientsNames;
        return this;
    }
}
