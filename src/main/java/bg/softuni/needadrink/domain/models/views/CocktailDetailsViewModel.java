package bg.softuni.needadrink.domain.models.views;

public class CocktailDetailsViewModel {

    private String id;

    private String name;

    private String description;

    private String preparation;

    private String imgUrl;

    private String videoUrl;




    public String getId() {
        return id;
    }

    public CocktailDetailsViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CocktailDetailsViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CocktailDetailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPreparation() {
        return preparation;
    }

    public CocktailDetailsViewModel setPreparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public CocktailDetailsViewModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public CocktailDetailsViewModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
