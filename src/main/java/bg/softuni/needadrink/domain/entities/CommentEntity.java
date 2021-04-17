package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {


    private String content;

    private LocalDateTime addedOn;

    private UserEntity author;



    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public CommentEntity setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public UserEntity getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }


    @Override
    public String toString() {
        return "CommentEntity{" +
                "content='" + content + '\'' +
                ", addedOn=" + addedOn +
                ", author=" + author +
                '}';
    }
}
