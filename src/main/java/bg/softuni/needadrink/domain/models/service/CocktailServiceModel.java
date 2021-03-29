package bg.softuni.needadrink.domain.models.service;

public class CocktailServiceModel {
    private String id;
    private String name;
    private String description;
    private String preparation;
    private String imgUrl;

    public String getId() {
        return id;
    }

    public CocktailServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CocktailServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CocktailServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPreparation() {
        return preparation;
    }

    public CocktailServiceModel setPreparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public CocktailServiceModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
