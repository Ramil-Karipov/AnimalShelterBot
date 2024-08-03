package pro.sky.telegrambot.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.*;
import pro.sky.telegrambot.service.impl.ReportServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ReportServiceImpl reportService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    AdaptationRepository adaptationRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    VolunteerRepository volunteerRepository;

    PetModel testPet;
    ClientModel testClient;
    VolunteerModel testVolunteer;
    AdaptationModel testAdaptation;
    ReportModel testReport;

    String testReportPhotoPath;
    String testReportTextContent;

    @BeforeEach
    void setUp() {
        testPet = restTemplate.getForObject("http://localhost:" + port +
                "/pet/create?name=testPet&birthDate=11.11.1111", PetModel.class);
        testClient = restTemplate.getForObject("http://localhost:" + port +
                "/client/create?name=testClient&phone=+7-999-999-99-99", ClientModel.class);
        testVolunteer = new VolunteerModel();
        testVolunteer.setId(1);
        testVolunteer.setName("testVolunteer");
        testVolunteer.setTelegramURL("TGUrl");
        testVolunteer.setChatId(11111111L);
        volunteerRepository.save(testVolunteer);
        testAdaptation = restTemplate.getForObject("http://localhost:" + port + "/adaptation/create?petId=" +
                testPet.getId() + "&clientId=" + testClient.getId() + "&volunteerId=1", AdaptationModel.class);
        testReportPhotoPath = "./src/test/resources/testReportPhoto/testReportPhoto1.jpg";
        testReportTextContent = "some info about pet";
        testReport = new ReportModel(1, LocalDate.now(), testClient.getId(), testPet.getId(), testReportPhotoPath,
                testReportTextContent, false);
    }

    @AfterEach
    void cleanUp() {
        reportRepository.deleteAll();
        adaptationRepository.deleteAll();
        volunteerRepository.deleteAll();
        petRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void add() {
        ResponseEntity<ReportModel> response = restTemplate.postForEntity("http://localhost:" + port + "/report/add",
                testReport, ReportModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update() {
        ResponseEntity<ReportModel> response = restTemplate.postForEntity("http://localhost:" + port + "/report/add",
                testReport, ReportModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReportModel updatedReport = response.getBody();
        updatedReport.setPetPhotoPath("otherPhoto");
        updatedReport.setPetInfo("otherText");
        RequestEntity<ReportModel> request = new RequestEntity<>(updatedReport, HttpMethod.PUT, URI.create(""));
        ResponseEntity<ReportModel> updatedResponse = restTemplate.exchange("http://localhost:" + port + "/report/update/" +
                response.getBody().getId(), HttpMethod.PUT, request, ReportModel.class);
        assertNotNull(updatedResponse);
        assertNotNull(updatedResponse.getBody());
        assertEquals(HttpStatus.OK, updatedResponse.getStatusCode());
        assertEquals("otherText", updatedResponse.getBody().getPetInfo());
        assertEquals("otherPhoto", updatedResponse.getBody().getPetPhotoPath());
    }

    @Test
    void updateAccepted() {
        ReportModel existingNotAcceptedReport = restTemplate.postForObject("http://localhost:" + port + "/report/add",
                testReport, ReportModel.class);
        RequestEntity<Boolean> request = new RequestEntity<>(true, HttpMethod.PUT, URI.create(""));
        ResponseEntity<ReportModel> updatedResponse = restTemplate.exchange("http://localhost:" + port + "/report/update-accepted/" +
                existingNotAcceptedReport.getId(), HttpMethod.PUT, request, ReportModel.class);
        assertNotNull(updatedResponse);
        assertNotNull(updatedResponse.getBody());
        assertEquals(HttpStatus.OK, updatedResponse.getStatusCode());
        assertEquals(true, updatedResponse.getBody().getIsAccepted());
    }

    @Test
    void getTextContent() {
        ReportModel existingReport = restTemplate.postForObject("http://localhost:" + port + "/report/add",
                testReport, ReportModel.class);
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/report/getText/" +
                existingReport.getId(), String.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("some info about pet", response.getBody());
    }

    @Test
    void downloadReportPhoto() throws IOException {
        ReportModel existingReport = restTemplate.postForObject("http://localhost:" + port + "/report/add",
                testReport, ReportModel.class);
        ResponseEntity<byte[]> response = restTemplate.getForEntity("http://localhost:" + port + "/report/download-photo/" +
                existingReport.getId(), byte[].class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Files.size(Path.of(testReportPhotoPath)), response.getBody().length);
        assertNotNull(response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentType().includes(MediaType.IMAGE_JPEG));
    }
}
