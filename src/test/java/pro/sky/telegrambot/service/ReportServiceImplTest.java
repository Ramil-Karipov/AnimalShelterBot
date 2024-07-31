package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.exception.PetNotFoundException;
import pro.sky.telegrambot.exception.ReportNotFoundException;
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.model.ReportModel;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.impl.ReportServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Mock
    ReportRepository reportRepository;
    @Mock
    ClientService clientServiceImpl;
    @Mock
    AdaptationService adaptationServiceImpl;

    @InjectMocks
    ReportServiceImpl reportService;

    ClientModel testClientWithPet;
    ClientModel testClientWithoutPet;
    PetModel testPetAdopted;
    PetModel testPetNotAdopted;
    AdaptationModel testAdaptation;
    ReportModel testReport;

    @BeforeEach
    void init() {
        testClientWithPet = new ClientModel();
        testClientWithPet.setId(1);
        testClientWithPet.setName("ClientWithPet");
        testClientWithPet.setPhone("+7-999-999-66-66");
        testClientWithPet.setChatId(777777L);
        testClientWithPet.setPetId(1);

        testClientWithoutPet = new ClientModel();
        testClientWithoutPet.setId(2);
        testClientWithoutPet.setName("ClientWithoutPet");
        testClientWithoutPet.setPhone("+7-999-999-66-66");
        testClientWithoutPet.setChatId(777777L);

        testPetAdopted = new PetModel();
        testPetAdopted.setId(1);
        testPetAdopted.setName("PetAdopted");
        testPetAdopted.setBirthDate(LocalDate.parse("01.01.2020", formatter));
        testPetAdopted.setAdopted(true);

        testPetNotAdopted = new PetModel();
        testPetNotAdopted.setId(2);
        testPetNotAdopted.setName("PetNotAdopted");
        testPetNotAdopted.setBirthDate(LocalDate.parse("02.02.2020", formatter));

        testAdaptation = new AdaptationModel(1, 1, 2, LocalDate.parse("11.11.1111", formatter),
                LocalDate.now().plusMonths(1));
        testAdaptation.setId(1);

        testReport = new ReportModel(1, LocalDate.now(), 1, 1,
                "no-photo", "Нет текста отчета", false);
    }

    @Test
    void createReportPositive() throws IOException {
        when(reportRepository.save(any(ReportModel.class))).thenReturn(testReport);
        when(clientServiceImpl.getClientByChatId(777777L)).thenReturn(testClientWithPet);

        ReportModel actualReport = reportService.createReport(null, "", 777777L);
        actualReport.setId(1);
        assertEquals(testReport, actualReport);
    }

    @Test
    void createReportNegativeClientHasNoPet() {
        when(clientServiceImpl.getClientByChatId(777777L)).thenReturn(testClientWithoutPet);
        assertThrows(PetNotFoundException.class, () ->
                reportService.createReport(null, "", 777777L)
        );
    }

    @Test
    void updateReport() {
        ReportModel existingReport = new ReportModel();
        when(reportRepository.findById(1)).thenReturn(Optional.of(existingReport));
        when(reportRepository.save(any(ReportModel.class))).thenReturn(testReport);
        ReportModel updatedReport = new ReportModel(1, LocalDate.now(), 1, 1, "photoPath", "petInfo", false);
        reportService.updateReport(1, updatedReport);
        assertEquals(updatedReport, existingReport);
    }

    @Test
    void updateAcceptedPositive() {
        when(reportRepository.findById(1)).thenReturn(Optional.of(testReport));
        when(adaptationServiceImpl.findAdaptationByPetId(1)).thenReturn(testAdaptation);
        when(adaptationServiceImpl.updateAdaptation(1, testAdaptation)).thenReturn(testAdaptation);
        when(reportRepository.save(any(ReportModel.class))).thenReturn(testReport);
        reportService.updateAccepted(1, true);
        assertTrue(testReport.getIsAccepted());
        assertEquals(testReport.getReportDate(), testAdaptation.getLastReportDate());

    }

    @Test
    void updateAcceptedNegative() {
        assertThrows(ReportNotFoundException.class, () ->
                reportService.updateAccepted(2, true)
        );
    }
}
