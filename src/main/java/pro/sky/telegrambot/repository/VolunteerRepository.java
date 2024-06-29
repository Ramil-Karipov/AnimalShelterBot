package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.VolunteerModel;

public interface VolunteerRepository extends JpaRepository<VolunteerModel, Integer> {

    @Query(value = "SELECT * FROM volunteer ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    VolunteerModel getRandomVolunteer();
}
