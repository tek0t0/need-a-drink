package bg.softuni.needadrink.domain.models.binding;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class CocktailInitBindingModel {

    private String id;

    @Expose
    private String name;

    @Expose
    private String description;

    @Expose
    private String imgUrl;

    @Expose
    private List<IngredientBindingModel> ingredients;


    public String getId() {
        return id;
    }

    public CocktailInitBindingModel setId(String id) {
        this.id = id;
        return this;
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
    @Length(min = 10, message = "Description length must be between 3 and 30 symbols!")
    public String getDescription() {
        return description;
    }

    public CocktailInitBindingModel setDescription(String description) {
        this.description = description;
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
}
