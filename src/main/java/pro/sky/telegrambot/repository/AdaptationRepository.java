package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.AdaptationModel;

public interface AdaptationRepository extends JpaRepository<AdaptationModel, Integer> {
}
