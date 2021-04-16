package bg.softuni.needadrink.domain.models.views;

import java.time.LocalDateTime;

public class CommentViewModel {

    private String content;

    private LocalDateTime addedOn;

    private String author;

    private String authorImgUrl;

    public String getContent() {
        return content;
    }

    public CommentViewModel setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public CommentViewModel setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CommentViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthorImgUrl() {
        return authorImgUrl;
    }

    public CommentViewModel setAuthorImgUrl(String authorImgUrl) {
        this.authorImgUrl = authorImgUrl;
        return this;
    }
}
