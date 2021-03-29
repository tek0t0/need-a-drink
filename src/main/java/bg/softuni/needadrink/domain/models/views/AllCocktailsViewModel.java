package bg.softuni.needadrink.domain.models.views;

public class AllCocktailsViewModel {
    private String id;
    private String name;
    private String description;
    private String imgUrl;

    public String getId() {
        return id;
    }

    public AllCocktailsViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AllCocktailsViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AllCocktailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public AllCocktailsViewModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
