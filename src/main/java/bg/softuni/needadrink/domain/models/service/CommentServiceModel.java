package bg.softuni.needadrink.domain.models.service;



import java.time.LocalDateTime;

public class CommentServiceModel {

    private String id;

    private String content;

    private LocalDateTime addedOn;

    private UserServiceModel author;



    public String getId() {
        return id;
    }

    public CommentServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CommentServiceModel setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public CommentServiceModel setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public CommentServiceModel setAuthor(UserServiceModel author) {
        this.author = author;
        return this;
    }
}
