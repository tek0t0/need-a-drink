package bg.softuni.needadrink.service.impl;


import bg.softuni.needadrink.domain.entities.LogEntity;
import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.repositiry.LogRepository;
import bg.softuni.needadrink.service.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public LogServiceModel seedLogInDB(LogServiceModel logServiceModel) {
        LogEntity log = this.modelMapper.map(logServiceModel, LogEntity.class);
        this.logRepository.saveAndFlush(log);
        return logServiceModel;
    }
}
