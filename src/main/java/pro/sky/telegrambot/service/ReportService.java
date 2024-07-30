package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.ReportModel;

import java.io.IOException;
import java.util.List;

/**
 * Сервис для работы с отчетами (создание, добавление, получение по id, редактирование, получение списка непринятых отчетов,
 * проставление отметки о прочтении)
 */
public interface ReportService {

    ReportModel createReport(byte[] petPhoto, String petInfo, Long chatId) throws IOException;

    ReportModel addReport(ReportModel model);

    ReportModel getReportById(Integer id);

    ReportModel updateReport(Integer id, ReportModel model);

    List<ReportModel> getAllNotAcceptedReports();

    ReportModel updateAccepted(Integer id, Boolean accepted);
}