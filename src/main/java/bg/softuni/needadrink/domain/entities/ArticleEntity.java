package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "articles")
public class ArticleEntity extends BaseEntity {

    private String title;

    private String coverImgUrl;

    private LocalDate addedOn;

    private String description;

    private String content;

    private List<CommentEntity> comments;

    public ArticleEntity() {
    }

    @Column(name = "title", nullable = false, unique = true)
    public String getTitle() {
        return title;
    }

    public ArticleEntity setTitle(String name) {
        this.title = name;
        return this;
    }

    @Column(name = "img_url")
    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public ArticleEntity setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
        return this;
    }

    @Column(name = "added_on", nullable = false)
    public LocalDate getAddedOn() {
        return addedOn;
    }

    public ArticleEntity setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public ArticleEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String getContent() {
        return content;
    }

    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<CommentEntity> getComments() {
        return comments;
    }

    public ArticleEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }
}
