package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.impl.ClientServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateClient() {
        ClientModel client = new ClientModel();
        client.setName("John Doe");
        client.setPhone("+1234567890");
        when(clientRepository.save(client)).thenReturn(client);

        ClientModel createdClient = clientService.createClient(client);

        verify(clientRepository, times(1)).save(client);
        assertNotNull(createdClient);
        assertEquals("John Doe", createdClient.getName());
    }

    @Test
    public void testGetClient() {
        ClientModel client = new ClientModel();
        client.setId(1);
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));

        Optional<ClientModel> retrievedClient = clientService.getClient(1);

        verify(clientRepository, times(1)).findById(1);
        assertTrue(retrievedClient.isPresent());
        assertEquals(1, retrievedClient.get().getId());
    }

    @Test
    public void testGetAllClients() {
        ClientModel client1 = new ClientModel();
        ClientModel client2 = new ClientModel();
        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<ClientModel> clients = clientService.getAllClients();

        verify(clientRepository, times(1)).findAll();
        assertEquals(2, clients.size());
    }

    @Test
    public void testUpdateClient() {
        ClientModel existingClient = new ClientModel();
        existingClient.setId(1);
        existingClient.setName("John Doe");

        ClientModel updatedDetails = new ClientModel();
        updatedDetails.setName("Jane Doe");
        updatedDetails.setPhone("+1234567890");

        when(clientRepository.findById(1)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        ClientModel updatedClient = clientService.updateClient(1, updatedDetails);

        verify(clientRepository, times(1)).findById(1);
        verify(clientRepository, times(1)).save(existingClient);
        assertEquals("Jane Doe", updatedClient.getName());
        assertEquals("+1234567890", updatedClient.getPhone());
    }

    @Test
    public void testDeleteClient() {
        ClientModel client = new ClientModel();
        client.setId(1);
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));

        clientService.deleteClient(1);

        verify(clientRepository, times(1)).findById(1);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    public void testGetClientByChatId() {
        ClientModel client = new ClientModel();
        client.setChatId(12345L);
        when(clientRepository.findFirstByChatId(12345L)).thenReturn(Optional.of(client));

        ClientModel retrievedClient = clientService.getClientByChatId(12345L);

        verify(clientRepository, times(1)).findFirstByChatId(12345L);
        assertNotNull(retrievedClient);
        assertEquals(12345L, retrievedClient.getChatId());
    }
}

