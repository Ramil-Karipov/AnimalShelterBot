package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.ClientModel;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientModel createClient(ClientModel model);

    ClientModel updateClient(Long id, ClientModel clientDetails);

    Optional<ClientModel> getClient(Long id);

    void deleteClient(Long id);

    List<ClientModel> getAllClients();
}
