package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.ClientModel;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientModel createClient(ClientModel model);

    ClientModel updateClient(Integer id, ClientModel clientDetails);

    Optional<ClientModel> getClient(Integer id);

    void deleteClient(Integer id);

    List<ClientModel> getAllClients();
}
