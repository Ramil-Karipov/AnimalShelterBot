package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AdaptationNotFoundException extends RuntimeException {

    public AdaptationNotFoundException() {
        super("Adaptation not found");
    }
}
