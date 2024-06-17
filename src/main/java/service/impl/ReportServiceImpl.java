package service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.ReportNotFoundException;
import pro.sky.telegrambot.model.ReportModel;
import pro.sky.telegrambot.repository.ReportRepository;
import service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public ReportModel addReport(ReportModel model) {
        return reportRepository.save(model);
    }

    @Override
    public ReportModel updateReport(Long id, ReportModel reportModel) {
        if (repostIsExist(id))
            return reportRepository.save(reportModel);

        throw new ReportNotFoundException();
    }

    @Override
    public boolean removeReport(Long id) {
        if (repostIsExist(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean repostIsExist(Long id) {
        return reportRepository.findById(id).isPresent();
    }
}