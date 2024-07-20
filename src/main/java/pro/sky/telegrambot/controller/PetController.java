package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
                            )
                    )
            }, tags = "Pets"
    )
    @GetMapping("/create")
    public PetModel create(@Parameter(description = "Строка, содержащая кличку питомца.")
                           @RequestParam String name,
                           @Parameter(description = "Строка, содержащая дату рождения питомца в формате: дд.мм.гггг")
                           @RequestParam String birthDate) {
        return petServiceImpl.createPet(name, birthDate);
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
}
