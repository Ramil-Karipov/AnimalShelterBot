package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.ReportModel;
import pro.sky.telegrambot.service.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Получить все непросмотренные отчеты",
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
            }, tags = "Reports"
    )
    @GetMapping("/getAllNotAccepted")
    public List<ReportModel> getReports() {
        return reportService.getAllNotAcceptedReports();
    }

    @Operation(
            summary = "Добавить новый отчет в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет добавлен",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ReportModel.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }, tags = "Reports"
    )
    @PostMapping("/add")
    public ReportModel addReport(@RequestBody ReportModel model) {
        return reportService.addReport(model);
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
            }, tags = "Reports"
    )
    @PutMapping("/update/{id}")
    public ReportModel updateReport(@PathVariable Integer id, @RequestBody ReportModel model) {
        return reportService.updateReport(id, model);
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
            }, tags = "Reports"
    )
    @PutMapping("/update-accepted/{id}")
    public ReportModel updateAccepted(@PathVariable Integer id, @RequestBody Boolean accepted) {
        return reportService.updateAccepted(id, accepted);
    }

    @Operation(
            summary = "Загрузить фото питомца по id отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фото загружено",
                            content = {
                                    @Content(
                                            mediaType = "image/jpeg"
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутрення ошибка сервера")
            }, tags = "Reports"
    )
    @GetMapping("/download-photo/{id}")
    public void downloadReportPhoto(@PathVariable int id, HttpServletResponse response) throws IOException {
        ReportModel report = reportService.getReportById(id);
        Path path = Path.of(report.getPetPhotoPath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {

            response.setContentType(Files.probeContentType(path));
            response.setContentLength((int) Files.size(path));
            response.setStatus(200);

            bis.transferTo(bos);
        }
    }
}
