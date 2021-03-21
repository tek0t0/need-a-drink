package bg.softuni.needadrink.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Article not found!")
public class ArticleNotFoundException extends RuntimeException {

    private int status;

    public ArticleNotFoundException() {
        this.status = 404;
    }

    public ArticleNotFoundException(String message) {
        super(message);
        this.status = 404;
    }

    public int getStatus() {
        return status;
    }
}
