package bg.softuni.needadrink.domain.models.binding;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class CocktailInitBindingModel {

    @Expose
    private String name;

    @Expose
    private String description;

    @Expose
    private String preparation;

    @Expose
    private String imgUrl;

    @Expose
    private String videoUrl;

    @Expose
    private List<IngredientBindingModel> ingredients;

    public CocktailInitBindingModel() {
        this.ingredients = new ArrayList<>();
    }

    @NotBlank
    @Length(min = 3, max = 30, message = "Name length must be between 3 and 30 symbols!")
    public String getName() {
        return name;
    }

    public CocktailInitBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    @NotBlank
    @Length(min = 10, message = "Description length must be more than 10 symbols!")
    public String getDescription() {
        return description;
    }

    public CocktailInitBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @NotBlank
    @Length(min = 10, message = "Preparation length must be more than 10 symbols!")
    public String getPreparation() {
        return preparation;
    }

    public CocktailInitBindingModel setPreparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    @Pattern(regexp = "^$|([^\\s]+(\\.(?i)(jpg|png|gif|bmp|svg))$)", message = "Add valid image URL!")
    public String getImgUrl() {
        return imgUrl;
    }

    public CocktailInitBindingModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public List<IngredientBindingModel> getIngredients() {
        return ingredients;
    }

    public CocktailInitBindingModel setIngredients(List<IngredientBindingModel> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public CocktailInitBindingModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    @Override
    public String toString() {
        return "CocktailInitBindingModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preparation='" + preparation + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
