package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
        super("Report not found");
    }
}