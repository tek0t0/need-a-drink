package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Empty cocktail DB!")
public class EmptyCocktailDataBaseError extends RuntimeException{

    private int status;

    public EmptyCocktailDataBaseError() {
        this.status = 500;
    }

    public EmptyCocktailDataBaseError(String message) {
        super(message);
        this.status = 500;
    }

    public int getStatus() {
        return status;
    }
}
