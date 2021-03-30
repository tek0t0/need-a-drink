package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Cocktail name already exists!")
public class CocktailNameAlreadyExists extends RuntimeException {

    private int status;

    public CocktailNameAlreadyExists() {
        this.status = 409;
    }

    public CocktailNameAlreadyExists(String message) {
        super(message);
        this.status = 409;
    }

    public int getStatus() {
        return status;
    }
}
