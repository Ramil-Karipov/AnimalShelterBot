package pro.sky.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.ClientService;

import java.util.regex.Pattern;
@Service
public class PhoneNumberValidatorImpl {

    private final ClientRepository clientRepository;

    public PhoneNumberValidatorImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    //Шаблон для проверки строк на соответствие формату номера телефона и имени клиента.
    private static final String PHONE_PATTERN = "^(\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}|8-9\\d{2}-\\d{3}-\\d{2}-\\d{2}|89\\d{9}|\\+79\\d{9}|79\\d{9}) - (.+)$";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);
    // Метод для проверки правильности формата номера телефона и имени.
    public static boolean isValid(String massage) {
        return pattern.matcher(massage).matches();
    }

    public static String formatPhoneNumber(String phoneNumber) {
        // Убираем любые символы кроме цифр
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        // Приводим номер к формату +7-9**-***-**-**
        if (phoneNumber.matches("^79\\d{9}$")) {
            return phoneNumber.replaceFirst("7(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "+7-$1-$2-$3-$4");
        } else if (phoneNumber.matches("^89\\d{9}$")) {
            return phoneNumber.replaceFirst("8(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "+7-$1-$2-$3-$4");
        }
        // Если номер не соответствует ни одному из форматов, выбрасываем исключение
        throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
    }
    //Метод для сохранения клиета в базе данных.
    public static ClientModel saveClient(String message, Long chatId, ClientService clientService) {
        // Разделение сообщения на части
        String[] parts = message.split(" - ", 2);
        String phoneNumber = parts[0].trim();
        String name = parts[1].trim();
        // Форматирование телефонного номера
        phoneNumber = formatPhoneNumber(phoneNumber);
        // Создание нового объекта клиента
        ClientModel client = new ClientModel();
        client.setPhone(phoneNumber);
        client.setName(name);
        client.setChatId(chatId);

        return clientService.createClient(client);

    }
    public  boolean isUserExist(String message) {
        String[] parts = message.split(" - ", 2);
        String phoneNumber = parts[0].trim();
        // Форматирование телефонного номера
        phoneNumber = formatPhoneNumber(phoneNumber);

        return clientRepository.findByPhone(phoneNumber).isPresent();
    }


}
