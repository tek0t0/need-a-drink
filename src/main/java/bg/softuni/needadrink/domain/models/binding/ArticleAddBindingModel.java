package bg.softuni.needadrink.domain.models.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class ArticleAddBindingModel {
    private String title;
    private String coverImgUrl;
    private String description;
    private String content;


    @NotBlank
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 symbols!")
    public String getTitle() {
        return title;
    }

    public ArticleAddBindingModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public ArticleAddBindingModel setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
        return this;
    }

    @NotBlank
    @Size(min = 5,  message = "Description must be more than 5 symbols!")
    public String getDescription() {
        return description;
    }

    public ArticleAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @NotBlank
    @Size(min = 10, message = "Content must be more than 10 symbols!")
    public String getContent() {
        return content;
    }

    public ArticleAddBindingModel setContent(String content) {
        this.content = content;
        return this;
    }
}
