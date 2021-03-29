package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Cocktail name already exists!")
public class CocktailNameAlreadyExists extends Throwable {

    private int status;

    public CocktailNameAlreadyExists() {
        this.status = 404;
    }

    public CocktailNameAlreadyExists(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
