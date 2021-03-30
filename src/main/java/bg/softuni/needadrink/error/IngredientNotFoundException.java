package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Ingredient not found!")
public class IngredientNotFoundException extends RuntimeException {
    private int status;

    public IngredientNotFoundException() {
        this.status = 404;
    }

    public IngredientNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
