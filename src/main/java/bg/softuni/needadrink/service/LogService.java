package bg.softuni.needadrink.service;


import bg.softuni.needadrink.domain.models.service.LogServiceModel;

public interface LogService {

    LogServiceModel seedLogInDB(LogServiceModel logServiceModel);
}
