package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.dto.ReportCreateDto;
import pro.sky.telegrambot.dto.ReportUpdateDto;
import pro.sky.telegrambot.model.ReportModel;
import pro.sky.telegrambot.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Получить все отчеты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты получены",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = ReportModel.class))
                                    )

                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "404", description = "Объект не найден"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }
    )
    @GetMapping
    public List<ReportModel> getReports() {
        return reportService.getAllReports();
    }

    @Operation(
            summary = "Создать новый отчет",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет создан",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ReportModel.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }
    )
    @PostMapping
    public ReportModel createReport(@RequestBody ReportCreateDto reportCreateDto) {
        return reportService.addReport(reportCreateDto);
    }

    @Operation(
            summary = "Изменить отчет",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет изменен",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ReportModel.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }
    )
    @PutMapping("/{id}")
    public ReportModel updateReport(@PathVariable Integer id, @RequestBody ReportUpdateDto reportUpdateDto) {
        return reportService.updateReport(id, reportUpdateDto);
    }

    @Operation(
            summary = "Изменить статус \"Принято\" у отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Параметр изменен",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ReportModel.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }
    )
    @PutMapping("/update-accepted/{id}")
    public ReportModel updateAccepted(@PathVariable Integer id, @RequestBody Boolean accepted) {
        return reportService.updateAccepted(id, accepted);
    }

    @Operation(
            summary = "Удалить отчет по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Параметр изменен",
                            content = {
                                    @Content(
                                            schema = @Schema(type = "boolean")
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }
    )
    @DeleteMapping("/{id}")
    public boolean deleteReport(@PathVariable Integer id) {
        return reportService.removeReport(id);
    }
}
