package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Cocktail with this Id not found!")
public class CocktailNotFoundException extends RuntimeException {

    private int status;

    public CocktailNotFoundException() {
        this.status = 404;
    }

    public CocktailNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
