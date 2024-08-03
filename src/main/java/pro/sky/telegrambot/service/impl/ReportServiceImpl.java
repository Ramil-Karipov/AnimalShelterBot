package pro.sky.telegrambot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.ClientNotFoundException;
import pro.sky.telegrambot.exception.PetNotFoundException;
import pro.sky.telegrambot.exception.ReportNotFoundException;
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.model.ReportModel;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.AdaptationService;
import pro.sky.telegrambot.service.ClientService;
import pro.sky.telegrambot.service.ReportService;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class ReportServiceImpl implements ReportService {

    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;
    private final ClientService clientServiceImpl;
    private final AdaptationService adaptationServiceImpl;

    public ReportServiceImpl(ReportRepository reportRepository, ClientService clientServiceImpl, @Lazy AdaptationService adaptationServiceImpl) {
        this.reportRepository = reportRepository;
        this.clientServiceImpl = clientServiceImpl;
        this.adaptationServiceImpl = adaptationServiceImpl;
    }

    /**
     * Метод для создания отчета на основании присланных в бот данных.
     * @param petPhoto - присланная в бот фотография в виде массива байт
     * @param petInfo - текстовое содержимое отчета
     * @param chatId - идентификатор чата в Telegram, из которого был прислан отчет
     */
    @Override
    public ReportModel createReport(byte[] petPhoto, String petInfo, Long chatId) throws ClientNotFoundException, PetNotFoundException, IOException {
        LocalDate reportDate = LocalDate.now();
        ClientModel client = clientServiceImpl.getClientByChatId(chatId);
        Integer clientId = client.getId();
        Integer petId = client.getPetId();
        String petPhotoPath = "no-photo";
        if (petId == null) {
            throw new PetNotFoundException();
        }
        if (petPhoto != null) {
            Path filePath = Path.of("./reportPhotos", "pet" + petId + "client" + clientId + "date" +
                    reportDate.format(PetModel.formatter).replaceAll("\\D", "") + ".jpg");
            petPhotoPath = filePath.toString();
            Files.deleteIfExists(filePath);

            try {
                Files.createDirectory(filePath.getParent());
            } catch (FileAlreadyExistsException e) {
                logger.debug("Директория для хранения фотографий отчетов уже создана");
            }

            try (InputStream is = new ByteArrayInputStream(petPhoto);
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW)
                 ) {
                is.transferTo(os);
            }
        }
        String reportText = "Нет текста отчета";
        if(petInfo != null && !petInfo.isBlank()) {
            reportText = petInfo;
        }

        ReportModel creatingReport = new ReportModel(0, reportDate, clientId, petId, petPhotoPath, reportText, false);
        reportRepository.save(creatingReport);
        return creatingReport;
    }

    /**
     * Метод для добавления отчета в базу данных.
     * Используется метод репозитория {@link ReportRepository#save(Object)}
     *
     * @param model {@link ReportModel}
     */
    @Override
    public ReportModel addReport(ReportModel model) {
        return reportRepository.save(model);
    }

    /**
     * Метод для изменения отчета в базе данных.
     * Используется метод репозитория {@link ReportRepository#save(Object)}
     *
     * @param id    id отчета для изменения
     * @param model {@link ReportModel}
     * @throws ReportNotFoundException - если отчет не найден
     */
    @Override
    public ReportModel updateReport(Integer id, ReportModel model) throws ReportNotFoundException {
        ReportModel existingReport = getReportById(id);

        existingReport.setId(model.getId());
        existingReport.setReportDate(model.getReportDate());
        existingReport.setPetId(model.getPetId());
        existingReport.setClientId(model.getClientId());
        existingReport.setPetPhotoPath(model.getPetPhotoPath());
        existingReport.setPetInfo(model.getPetInfo());
        existingReport.setIsAccepted(model.getIsAccepted());

        reportRepository.save(existingReport);
        return existingReport;

    }

    /**
     * Метод для получения списка всех непринятых отчетов из базы.
     * Используется метод репозитория {@link ReportRepository#findAllByIsAccepted(Boolean)}
     */
    @Override
    public List<ReportModel> getAllNotAcceptedReports() {
        return reportRepository.findAllByIsAccepted(false);
    }

    /**
     * Метод для обновления свойства accepted.
     * Используется метод репозитория {@link ReportRepository#save(Object)}
     *
     * @param id       id изменяемого отчета
     * @param accepted параметр для изменения
     * @throws ReportNotFoundException - если отчет не найден
     */
    @Override
    public ReportModel updateAccepted(Integer id, Boolean accepted) throws ReportNotFoundException {
        ReportModel model = getReportById(id);
        if (model != null) {
            model.setIsAccepted(accepted);
            AdaptationModel connectedAdaptation = adaptationServiceImpl.findAdaptationByPetId(model.getPetId());
            connectedAdaptation.setLastReportDate(model.getReportDate());
            adaptationServiceImpl.updateAdaptation(connectedAdaptation.getId(), connectedAdaptation);
            reportRepository.save(model);
            return model;
        }

        throw new ReportNotFoundException();
    }

    /**
     * Получение отчета по id.
     * Используется метод репозитория {@link ReportRepository#findById(Object)}
     *
     * @param id id отчета
     * @throws ReportNotFoundException - если отчет не найден
     */
    public ReportModel getReportById(Integer id) throws ReportNotFoundException {
        return reportRepository.findById(id).orElseThrow(ReportNotFoundException::new);
    }
}