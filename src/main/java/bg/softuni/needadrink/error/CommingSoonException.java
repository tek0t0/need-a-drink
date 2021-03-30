package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Comming Soon!")
public class CommingSoonException extends RuntimeException{

    private int status;

    public CommingSoonException() {
        this.status = 404;
    }

    public CommingSoonException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
