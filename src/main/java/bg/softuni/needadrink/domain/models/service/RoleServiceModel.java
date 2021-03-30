package bg.softuni.needadrink.domain.models.service;

public class RoleServiceModel {

    private String id;

    private String name;

    public RoleServiceModel() {
    }

    public String getId() {
        return id;
    }

    public RoleServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoleServiceModel setName(String name) {
        this.name = name;
        return this;
    }
}
