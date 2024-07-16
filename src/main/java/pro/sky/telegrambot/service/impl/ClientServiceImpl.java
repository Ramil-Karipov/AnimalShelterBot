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
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientModel createClient(ClientModel clientModel) {
        return clientRepository.save(clientModel);
    }

    public Optional<ClientModel> getClient(Integer id) {
        return clientRepository.findById(id);
    }

    public List<ClientModel> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientModel updateClient(Integer id, ClientModel clientDetails) {
        ClientModel client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        client.setName(clientDetails.getName());
        client.setPhone(clientDetails.getPhone());
        client.setChatId(clientDetails.getChatId());
        client.setPetId(clientDetails.getPetId());
        return clientRepository.save(client);
    }

    public void deleteClient(Integer id) {
        ClientModel client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.delete(client);
    }

    public ClientModel getClientByChatId(Long chatId) {
        Optional<ClientModel> optionalClient = clientRepository.findByChatId(chatId);
        return optionalClient.orElse(null); // Или выбросьте исключение, если клиент не найден
    }

}
