package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.ClientModel;

public interface ClientRepository extends JpaRepository<ClientModel, Long> {
}
