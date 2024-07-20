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
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.service.AdaptationService;

@RestController
@RequestMapping("/adaptation")
public class AdaptationController {

    private final AdaptationService adaptationServiceImpl;

    public AdaptationController(AdaptationService adaptationServiceImpl) {
        this.adaptationServiceImpl = adaptationServiceImpl;
    }

    @Operation(
            summary = "Создание нового процесса адаптации для питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Созданный процесс адаптации",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdaptationModel.class)
                            )),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "В случае, если в БД по переданным id не найдены питомец или клиент."
                            )
            }, tags = "Adaptations"
    )
    @GetMapping("/create")
    public AdaptationModel create(@Parameter(description = "Идентификатор питомца, для которого создается процесс адаптации." +
            " Соответствует значению поля id из таблицы pet. Целое положительное число.")
                                  @RequestParam Integer petId,
                                  @Parameter(description = "Идентификатор клиента, забравшего питомца из приюта для адаптации." +
                                          " Соответствует значению поля id из таблицы client. Целое положительное число.")
                                  @RequestParam Integer clientId,
                                  @Parameter(description = "Идентификатор волонтера, назначенного ответственным за процесс адаптации." +
                                          " Соответствует значению поля id из таблицы volunteer. Целое положительное число в диапазоне 1-4")
                                  @RequestParam Integer volunteerId) {
        return adaptationServiceImpl.createAdaptation(petId, clientId, volunteerId);
    }

    @Operation(
            summary = "Продление существующего процесса адаптации для питомца на указанное количество дней.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Процесс адаптации с новой датой завершения",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdaptationModel.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В случае, если для питомца не найден активный процесс адаптации."
                    )
            }, tags = "Adaptations"
    )
    @GetMapping("/extend")
    public AdaptationModel extend(@Parameter(description = "Идентификатор питомца, для которого существует активный процесс адаптации." +
            " Соответствует значению поля pet_id из таблицы adaptation. Целое положительное число.")
                                  @RequestParam Integer petId,
                                  @Parameter(description = "Количество дней, на которое будет продлен процесс адаптации." +
                                          "Целое положительное число.")
                                  @RequestParam Integer days) {
            return adaptationServiceImpl.extendAdaptation(petId, days);
    }

    @Operation(
            summary = "Досрочное прерывание активного процесса адаптации для питомца.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Досрочное прерывание процесса адаптации."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "В случае, если для питомца не найден активный процесс адаптации."
                    )
            }, tags = "Adaptations"
    )
    @GetMapping("/abort")
    public void abort(@Parameter(description = "Идентификатор питомца, для которого существует активный процесс адаптации." +
            " Соответствует значению поля pet_id из таблицы adaptation. Целое положительное число.")
                          @RequestParam Integer petId) {
        adaptationServiceImpl.abortAdaptation(petId);
    }
}
