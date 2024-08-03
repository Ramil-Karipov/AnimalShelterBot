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
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.ClientService;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    ClientService clientServiceImpl;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TestRestTemplate restTemplate;

    private final String clientName = "testClient";
    private final String clientPhone = "+7-999-999-99-99";

    @Test
    void createPositive() {
        ResponseEntity<ClientModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/client/create?name=" + clientName + "&phone=" + clientPhone, ClientModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ClientModel createdClient = response.getBody();

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/client/find?id=" + createdClient.getId(), ClientModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        clientRepository.deleteAll();
    }

    @Test
    void createNegative() {
        ResponseEntity<ClientModel> response = restTemplate.getForEntity("http://localhost:" + port +
                "/client/create?name=" + clientName + "&phone=8999999", ClientModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/client/find?id=1", ClientModel.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void update() {
        ClientModel existingClient = restTemplate.getForObject("http://localhost:" + port +
                "/client/create?name=" + clientName + "&phone=" + clientPhone, ClientModel.class);

        ClientModel updatingClient = new ClientModel();
        updatingClient.setId(existingClient.getId());
        updatingClient.setName(clientName);
        updatingClient.setPhone("+7-000-000-00-00");

        RequestEntity<ClientModel> requestEntity = new RequestEntity<>(updatingClient, HttpMethod.PUT, URI.create(""));
        ResponseEntity<ClientModel> response = restTemplate.exchange("http://localhost:" + port +
                "/client/update/" + existingClient.getId(), HttpMethod.PUT, requestEntity, ClientModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = restTemplate.getForEntity("http://localhost:" + port +
                "/client/find?id=" + updatingClient.getId(), ClientModel.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("+7-000-000-00-00", response.getBody().getPhone());

        clientRepository.deleteAll();
    }
}
