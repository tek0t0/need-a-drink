package bg.softuni.needadrink.domain.models.binding;

import com.google.gson.annotations.Expose;

public class IngredientBindingModel {

    @Expose
    private String name;

    @Expose
    private String imgUrl;

    public String getName() {
        return name;
    }

    public IngredientBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public IngredientBindingModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
