package pro.sky.telegrambot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.service.PetService;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PetControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    PetService petServiceImpl;

    @Autowired
    TestRestTemplate restTemplate;

    private final String petName = "testPet";
    private final String petBirthDate = "11.11.1111";

    @Test
    void createPositive() {
        ResponseEntity<PetModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/create?name=" + petName + "&birthDate=" + petBirthDate, PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/find?id=1", PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createNegative() {
        ResponseEntity<PetModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/create?name=" + petName + "&birthDate=11-11-1111", PetModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/find?id=1", PetModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void update() {
        PetModel existingPet = restTemplate.getForObject("http://localhost:" + port +
                "/pet/create?name=" + petName + "&birthDate=" + petBirthDate, PetModel.class);

        PetModel newPet = new PetModel(LocalDate.now(), "justBorn");
        newPet.setId(1);

        RequestEntity<PetModel> requestEntity = new RequestEntity<>(newPet, HttpMethod.PUT, URI.create(""));
        ResponseEntity<PetModel> response = restTemplate.exchange("http://localhost:" + port +
                "/pet/update/1", HttpMethod.PUT, requestEntity, PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/find?id=1", PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LocalDate.now(), response.getBody().getBirthDate());
    }
}
