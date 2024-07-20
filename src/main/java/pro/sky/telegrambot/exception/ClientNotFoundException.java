package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException() {
        super("Client not found.");
    }
}
