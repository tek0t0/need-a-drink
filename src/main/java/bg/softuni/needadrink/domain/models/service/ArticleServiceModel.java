package bg.softuni.needadrink.domain.models.service;

import bg.softuni.needadrink.domain.models.views.CommentViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArticleServiceModel {

    private String id;

    private String title;

    private String coverImgUrl;

    private LocalDate addedOn;

    private String description;

    private String content;

    private List<CommentViewModel> comments;

    public ArticleServiceModel() {
        this.comments = new ArrayList<>();
    }

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

    public List<CommentViewModel> getComments() {
        return comments;
    }

    public ArticleServiceModel setComments(List<CommentViewModel> comments) {
        this.comments = comments;
        return this;
    }
}
