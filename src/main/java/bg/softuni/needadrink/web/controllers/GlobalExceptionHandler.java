package bg.softuni.needadrink.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({Throwable.class})
    public String handleException(Throwable ex, Model model){

        LOGGER.error("Exception", ex);

        Throwable throwable = ex;

        while (throwable.getCause() != null){
            throwable = throwable.getCause();
        }

        model.addAttribute("message",ex.getMessage());

        return "/error";
    }
}
