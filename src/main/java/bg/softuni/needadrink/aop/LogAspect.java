package bg.softuni.needadrink.aop;


import bg.softuni.needadrink.domain.models.service.LogServiceModel;
import bg.softuni.needadrink.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {

    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("within(bg.softuni.needadrink.service..*)")
    public void afterThrowing(){}

    @AfterThrowing("afterThrowing()")
    public void afterThrowingAdvice(JoinPoint joinPoint){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String action = joinPoint.getSignature().getName();

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(auth.getName());
        logServiceModel.setDescription(action + "method failed.");
        this.logService.seedLogInDB(logServiceModel);


    }
}
