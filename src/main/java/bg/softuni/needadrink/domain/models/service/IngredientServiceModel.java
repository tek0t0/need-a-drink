package bg.softuni.needadrink.domain.models.service;

public class IngredientServiceModel {

    private String id;

    private String name;

    private String imgUrl;

    public String getId() {
        return id;
    }

    public IngredientServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IngredientServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public IngredientServiceModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
