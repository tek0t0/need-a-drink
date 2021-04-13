package bg.softuni.needadrink.domain.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class LogEntity extends BaseEntity{

    private String username;

    private String description;

    private LocalDateTime time;

    public LogEntity() {
    }

    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }

    public LogEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public LogEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "time", nullable = false)
    public LocalDateTime getTime() {
        return time;
    }

    public LogEntity setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }
}
