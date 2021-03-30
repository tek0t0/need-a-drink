package bg.softuni.needadrink.domain.models.service;

import java.time.LocalDate;

public class ArticleServiceModel {

    private String id;

    private String title;

    private String coverImgUrl;

    private LocalDate addedOn;

    private String description;

    private String content;

    public String getId() {
        return id;
    }

    public ArticleServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticleServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public ArticleServiceModel setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
        return this;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public ArticleServiceModel setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ArticleServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleServiceModel setContent(String content) {
        this.content = content;
        return this;
    }
}
