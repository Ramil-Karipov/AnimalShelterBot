package pro.sky.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.ClientService;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ClientModel createClient(ClientModel clientModel) {
        return clientRepository.save(clientModel);
    }

    public Optional<ClientModel> getClient(Long id) {
        return clientRepository.findById(id);
    }

    public List<ClientModel> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientModel updateClient(Long id, ClientModel clientDetails) {
        ClientModel client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        client.setName(clientDetails.getName());
        client.setPhone(clientDetails.getPhone());
        client.setChat_id(clientDetails.getChat_id());
        client.setPet_id(clientDetails.getPet_id());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        ClientModel client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.delete(client);
    }
}
