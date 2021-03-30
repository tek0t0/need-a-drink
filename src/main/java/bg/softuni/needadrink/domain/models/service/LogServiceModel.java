package bg.softuni.needadrink.domain.models.service;

import java.time.LocalDateTime;

public class LogServiceModel {

    private String id;

    private String username;

    private String description;

    private LocalDateTime time;

    public LogServiceModel() {
    }

    public String getId() {
        return id;
    }

    public LogServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
