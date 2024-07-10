package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.ClientModel;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientModel, Integer> {
    Optional<ClientModel> findByChatId(Long chatId);
}
