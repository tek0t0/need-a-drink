package bg.softuni.needadrink.domain.models.binding;

import java.time.LocalDate;

public class ArticleAddBindingModel {
    private String title;
    private String coverImgUrl;
    private LocalDate addedOn;
    private String description;
    private String content;

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

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public ArticleAddBindingModel setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ArticleAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleAddBindingModel setContent(String content) {
        this.content = content;
        return this;
    }
}
