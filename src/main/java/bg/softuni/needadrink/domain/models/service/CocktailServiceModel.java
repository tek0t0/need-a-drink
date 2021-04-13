package bg.softuni.needadrink.domain.models.service;

import java.util.List;
import java.util.Set;

public class CocktailServiceModel {

    private String id;

    private String name;

    private String description;

    private String preparation;

    private String imgUrl;

    private String videoUrl;

    private List<String> ingredientsNames;

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

    public List<String> getIngredientsNames() {
        return ingredientsNames;
    }

    public CocktailServiceModel setIngredientsNames(List<String> ingredientsNames) {
        this.ingredientsNames = ingredientsNames;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public CocktailServiceModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
