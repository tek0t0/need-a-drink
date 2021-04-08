package bg.softuni.needadrink.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "articles")
public class ArticleEntity extends BaseEntity {

    private String title;

    private String coverImgUrl;

    private LocalDate addedOn;

    private String description;

    private String content;

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
}
