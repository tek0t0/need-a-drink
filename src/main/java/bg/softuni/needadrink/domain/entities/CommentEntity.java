package bg.softuni.needadrink.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(name = "added_on",nullable = false)
    private LocalDateTime addedOn;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private ArticleEntity article;
}
