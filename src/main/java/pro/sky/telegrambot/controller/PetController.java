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
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.service.PetService;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petServiceImpl;

    public PetController(PetService petServiceImpl) {
        this.petServiceImpl = petServiceImpl;
    }

    @Operation(
            summary = "Создание новой записи в БД о питомце",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданный в БД питомец",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PetModel.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В случае некорректного ввода клички питомца или даты рождения."
                    )
            }, tags = "Pets"
    )
    @GetMapping("/create")
    public PetModel create(@Parameter(description = "Строка, содержащая кличку питомца.")
                           @RequestParam String name,
                           @Parameter(description = "Строка, содержащая дату рождения питомца в формате: дд.мм.гггг")
                           @RequestParam String birthDate) {
        try {
            return petServiceImpl.createPet(name, birthDate);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @Operation(
            summary = "Получение питомца из БД по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный питомец",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PetModel.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В случае, если питомец не найден."
                    )

            }, tags = "Pets"
    )
    @GetMapping("/find")
    public PetModel findPet(@Parameter(description = "Идентификатор питомца в БД. Целое положительное число.")
                            @RequestParam Integer id) {
        return petServiceImpl.findById(id);
    }

    @Operation(
            summary = "Изменить запись в БД о питомце.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись изменена",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = PetModel.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "В случае, если питомец не найден"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }, tags = "Pets"
    )
    @PutMapping("/update/{id}")
    public PetModel updatePet(@Parameter(description = "Идентификатор питомца в БД. Целое положительное число.")
                                    @PathVariable int id,
                                    @RequestBody PetModel model) {
        return petServiceImpl.updatePet(id, model);
    }
}
