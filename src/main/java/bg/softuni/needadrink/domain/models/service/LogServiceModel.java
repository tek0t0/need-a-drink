package bg.softuni.needadrink.domain.models.service;

import java.time.LocalDateTime;

public class LogServiceModel {

    private String id;

    private String username;

    private String description;

    private LocalDateTime time;

    public LogServiceModel() {
        this.time = LocalDateTime.now();
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

    public LogServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LogServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public LogServiceModel setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }
}
