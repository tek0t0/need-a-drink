package bg.softuni.needadrink.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article extends BaseEntity{
    private String title;
    private UserEntity author;
    private String coverImgUrl;
    private LocalDateTime addedOn;
    private String description;
    private String content;

    public Article() {
    }

    @Column(name = "title", nullable = false, unique = true)
    public String getTitle() {
        return title;
    }

    public Article setTitle(String name) {
        this.title = name;
        return this;
    }

    @ManyToOne
    public UserEntity getAuthor() {
        return author;
    }

    public Article setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    @Column(name = "img_url")
    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public Article setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
        return this;
    }


    @Column(name = "added_on")
    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public Article setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public Article setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "content",columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public Article setContent(String content) {
        this.content = content;
        return this;
    }
}
