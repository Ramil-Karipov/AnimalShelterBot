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
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.PetService;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    PetService petServiceImpl;

    @Autowired
    PetRepository petRepository;

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

        PetModel createdPet = response.getBody();

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/find?id=" + createdPet.getId(), PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        petRepository.deleteAll();
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
                "/pet/update/" + existingPet.getId(), HttpMethod.PUT, requestEntity, PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/pet/find?id=" + existingPet.getId(), PetModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LocalDate.now(), response.getBody().getBirthDate());

        petRepository.deleteAll();
    }
}
