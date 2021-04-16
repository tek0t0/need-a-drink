package bg.softuni.needadrink.domain.models.binding;

import javax.validation.constraints.NotBlank;

public class CommentBindingModel {

    private String content;

    @NotBlank
    public String getContent() {
        return content;
    }

    public CommentBindingModel setContent(String content) {
        this.content = content;
        return this;
    }
}
