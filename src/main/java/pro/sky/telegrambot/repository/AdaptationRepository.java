package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.AdaptationModel;

import java.util.Optional;

public interface AdaptationRepository extends JpaRepository<AdaptationModel, Integer> {
    @Query(value = "SELECT * FROM adaptation WHERE pet_id = :petId AND is_finished = false", nativeQuery = true)
    Optional<AdaptationModel> findAdaptationByPetId(Integer petId);
}
