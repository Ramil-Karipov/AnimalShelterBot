package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.ReportModel;

/*
* Сервис для работы с отчетами (добавление, редактирование, удаление)
*/
public interface ReportService {
    ReportModel addReport(ReportModel model);

    ReportModel updateReport(Long id, ReportModel reportModel);

    boolean removeReport(Long id);
}