package pro.sky.telegrambot.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.model.VolunteerModel;
import pro.sky.telegrambot.repository.AdaptationRepository;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.VolunteerRepository;
import pro.sky.telegrambot.service.impl.AdaptationServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class AdaptationControllerTest {

    @LocalServerPort
    public int port;

    @Mock
    TelegramBotUpdatesListener listener;

    @Autowired
    @InjectMocks
    AdaptationServiceImpl adaptationService;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AdaptationRepository adaptationRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    VolunteerRepository volunteerRepository;

    PetModel testPet;
    ClientModel testClient;
    VolunteerModel testVolunteer;

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
        testVolunteer.setChatId(111111111L);
        volunteerRepository.save(testVolunteer);
    }

    @AfterEach
    void cleanUp() {
        adaptationRepository.deleteAll();
        volunteerRepository.deleteAll();
        petRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void createPositive() {
        ResponseEntity<AdaptationModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/adaptation/create?petId=" + testPet.getId() + "&clientId=" + testClient.getId() + "&volunteerId=1", AdaptationModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        testClient = restTemplate.getForObject("http://localhost:" + port + "/client/find?id=" + testClient.getId(),
                ClientModel.class);
        assertEquals(testPet.getId(), testClient.getPetId());
        testPet = restTemplate.getForObject("http://localhost:" + port + "/pet/find?id=" + testPet.getId(),
                PetModel.class);
        assertTrue(testPet.getAdopted());
    }

    @Test
    void createNegative() {
        ResponseEntity<AdaptationModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/adaptation/create?petId=2&clientId=2&volunteerId=1", AdaptationModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void extendPositive() {
        AdaptationModel existingAdaptation = restTemplate.getForObject("http://localhost:" + port +
                "/adaptation/create?petId=" + testPet.getId() + "&clientId=" + testClient.getId() + "&volunteerId=1", AdaptationModel.class);
        AdaptationModel extendedBy10Adaptation = restTemplate.getForObject("http://localhost:" + port + "/adaptation/extend?petId=" + testPet.getId() +
                "&days=10", AdaptationModel.class);
        assertEquals(existingAdaptation.getFinishDate().plusDays(10), extendedBy10Adaptation.getFinishDate());
    }

    @Test
    void extendNegative() {
        AdaptationModel existingAdaptation = restTemplate.getForObject("http://localhost:" + port +
                "/adaptation/create?petId=" + testPet.getId() + "&clientId=" + testClient.getId() + "&volunteerId=1", AdaptationModel.class);
        ResponseEntity<AdaptationModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/adaptation/extend?petId=" + testPet.getId() + "&days=0", AdaptationModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void abort() {
        AdaptationModel existingAdaptation = restTemplate.getForObject("http://localhost:" + port +
                "/adaptation/create?petId=" + testPet.getId() + "&clientId=" + testClient.getId() + "&volunteerId=1", AdaptationModel.class);
        testClient = restTemplate.getForObject("http://localhost:" + port + "/client/find?id=" + testClient.getId(),
                ClientModel.class);
        assertEquals(testPet.getId(), testClient.getPetId());
        testPet = restTemplate.getForObject("http://localhost:" + port + "/pet/find?id=" + testPet.getId(),
                PetModel.class);
        assertTrue(testPet.getAdopted());
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port +
                "/adaptation/abort?petId=" + testPet.getId(), Object.class);
        testClient = restTemplate.getForObject("http://localhost:" + port + "/client/find?id=" + testClient.getId(),
                ClientModel.class);
        assertNull(testClient.getPetId());
        testPet = restTemplate.getForObject("http://localhost:" + port + "/pet/find?id=" + testPet.getId(),
                PetModel.class);
        assertFalse(testPet.getAdopted());
    }
}
