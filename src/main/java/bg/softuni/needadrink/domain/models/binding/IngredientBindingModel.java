package bg.softuni.needadrink.domain.models.binding;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class IngredientBindingModel {

    @Expose
    private String name;

    @Expose
    private String imgUrl;

    @NotBlank
    @Length(min = 3, max = 30, message = "Name length must be between 3 and 30 symbols!")
    public String getName() {
        return name;
    }


    public IngredientBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    @Pattern(regexp = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)", message = "Add valid image URL!")
    public String getImgUrl() {
        return imgUrl;
    }

    public IngredientBindingModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
