package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.ReportCreateDto;
import pro.sky.telegrambot.dto.ReportUpdateDto;
import pro.sky.telegrambot.exception.ReportNotFoundException;
import pro.sky.telegrambot.model.ReportModel;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.ReportService;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Метод для добавления отчета в базу данных
     * Ипользуется метод репозитория {@link ReportRepository#save(Object)}
     * @param reportCreateDto dto для создания эклемпляра Report
     * */
    @Override
    public ReportModel addReport(ReportCreateDto reportCreateDto) {
        ReportModel model = createDtoToModel(reportCreateDto);

        return reportRepository.save(model);
    }

    /**
     * Метод для изменения отчета в базе данных
     * Ипользуется метод репозитория {@link ReportRepository#save(Object)}
     * @param id id отчета для изменения
     * @param reportUpdateDto dto для создания эклемпляра Report
     * */
    @Override
    public ReportModel updateReport(Integer id, ReportUpdateDto reportUpdateDto) {
        ReportModel model = getReportById(id);
        if (model != null) {

            if (reportUpdateDto.getClientId() != null)
                model.setClientId(reportUpdateDto.getClientId());

            if (reportUpdateDto.getReportDate() != null)
                model.setReportDate(reportUpdateDto.getReportDate());

            if (reportUpdateDto.getPetInfo() != null)
                model.setPetInfo(reportUpdateDto.getPetInfo());

            if (reportUpdateDto.getPetId() != null)
                model.setPetId(reportUpdateDto.getPetId());

            if (reportUpdateDto.getAccepted() != null)
                model.setAccepted(reportUpdateDto.getAccepted());

            if (reportUpdateDto.getPetPhotoPath() != null)
                model.setPetPhotoPath(reportUpdateDto.getPetPhotoPath());

            return reportRepository.save(model);
        }

        throw new ReportNotFoundException();
    }

    /**
     * Метод для удаления отчета из базы данных
     * Ипользуется метод репозитория {@link ReportRepository#deleteById(Object)}
     * @param id id отчета для удаления
     * */
    @Override
    public boolean removeReport(Integer id) {
        if (reportIsExist(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод для получения списка всех отчетов из базы
     * Ипользуется метод репозитория {@link ReportRepository#findAll()}
     * */
    @Override
    public List<ReportModel> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Метод для обновления свойства accepted
     * Ипользуется метод репозитория {@link ReportRepository#save(Object)}
     * @param id id изменяемого отчета
     * @param accepted пааметр accepted для изменения
     * */
    @Override
    public ReportModel updateAccepted(Integer id, Boolean accepted) {
        ReportModel model = getReportById(id);
        if (model != null) {
            model.setAccepted(accepted);
            reportRepository.save(model);
            return model;
        }

        throw new ReportNotFoundException();
    }

    /**
     * Проверка отчета на существование по id
     * Ипользуется метод репозитория {@link ReportRepository#findById(Object)}
     * @param id id проверяемого отчета
     * */
    private boolean reportIsExist(Integer id) {
        return reportRepository.findById(id).isPresent();
    }

    /**
     * Получение отчета по id
     * Ипользуется метод репозитория {@link ReportRepository#findById(Object)}
     * @param id id проверяемого отчета
     * */
    private ReportModel getReportById(Integer id) {
        return reportRepository.findById(id).orElse(null);
    }

    /**
     * Метод преобразование ReportCreateDto в ReportModel
     *
     * @param reportCreateDto dto для преобразования в model
     * */
    private ReportModel createDtoToModel(ReportCreateDto reportCreateDto) {
        ReportModel model = new ReportModel();

        model.setPetId(reportCreateDto.getPetId());
        model.setClientId(reportCreateDto.getClientId());
        model.setPetInfo(reportCreateDto.getPetInfo());
        model.setPetPhotoPath(reportCreateDto.getPetPhotoPath());

        return model;
    }
}