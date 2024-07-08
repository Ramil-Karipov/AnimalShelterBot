package pro.sky.telegrambot.service;

import pro.sky.telegrambot.dto.ReportCreateDto;
import pro.sky.telegrambot.dto.ReportUpdateDto;
import pro.sky.telegrambot.model.ReportModel;

import java.util.List;

/**
* Сервис для работы с отчетами (добавление, редактирование, удаление)
*/
public interface ReportService {
    ReportModel addReport(ReportCreateDto model);

    ReportModel updateReport(Integer id, ReportUpdateDto reportUpdateDto);

    boolean removeReport(Integer id);

    List<ReportModel> getAllReports();

    ReportModel updateAccepted(Integer id, Boolean accepted);
}