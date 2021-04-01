package bg.softuni.needadrink.web.controllers;

import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
    }

    @ExceptionHandler({Throwable.class})
    public String handleException(Throwable ex, Model model){

        LOGGER.error("Exception", ex);
        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername("ADMIN");
        logServiceModel.setDescription(ex.getMessage());
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);


        Throwable throwable = ex;

        while (throwable.getCause() != null){
            throwable = throwable.getCause();
        }

        model.addAttribute("message",throwable.getMessage());

        return "/error";
    }
}
