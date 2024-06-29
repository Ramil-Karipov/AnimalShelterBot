package pro.sky.telegrambot.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException() {
        super("Питомец не найден");
    }
}
