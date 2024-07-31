package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pro.sky.telegrambot.exception.ClientNotFoundException;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.service.ClientService;
import pro.sky.telegrambot.service.impl.PhoneNumberValidatorImpl;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientServiceImpl;

    public ClientController(ClientService clientServiceImpl, PhoneNumberValidatorImpl phoneNumberValidator) {
        this.clientServiceImpl = clientServiceImpl;
    }

    @Operation(
            summary = "Создание новой записи в БД о клиенте",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданный в БД клиент",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClientModel.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В случае некорректного ввода имени клиента или номера телефона."
                    )
            }, tags = "Clients"
    )
    @GetMapping("/create")
    public ClientModel create(@Parameter(description = "Строка, содержащая имя клиента.")
                              @RequestParam String name,
                              @Parameter(description = "Номер телефона клиента в формате: +7-999-999-99-99")
                              @RequestParam String phone,
                              @Parameter(description = "ChatId клиента в Telegram (если не вводить, будет присвоено значение" +
                                      "по умолчанию chatId = 0)")
                              @RequestParam(required = false) Long chatId) {
        try {
            long clientChatId = 0L;
            if (chatId != null) {
                clientChatId = chatId;
            }
            String formattedPhone = PhoneNumberValidatorImpl.formatPhoneNumber(phone);
            ClientModel client = new ClientModel();
            client.setName(name);
            client.setPhone(formattedPhone);
            client.setChatId(clientChatId);
            return clientServiceImpl.createClient(client);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @Operation(
            summary = "Получение клиента из БД по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный клиент",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClientModel.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В случае, если клиент не найден."
                    )

            }, tags = "Clients"
    )
    @GetMapping("/find")
    public ClientModel findClient(@Parameter(description = "Идентификатор клиента в БД. Целое положительное число.")
                                  @RequestParam Integer id) {
        return clientServiceImpl.getClient(id).orElseThrow(ClientNotFoundException::new);
    }

    @Operation(
            summary = "Изменить запись в БД о клиенте.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись изменена",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ClientModel.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "В случае, если клиент не найден"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }, tags = "Clients"
    )
    @PutMapping("/update/{id}")
    public ClientModel updateReport(@Parameter(description = "Идентификатор клиента в БД. Целое положительное число.")
                                    @PathVariable int id,
                                    @RequestBody ClientModel model) {
        return clientServiceImpl.updateClient(id, model);
    }
}
