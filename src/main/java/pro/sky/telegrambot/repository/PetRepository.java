package pro.sky.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.PetModel;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetModel, Integer> {
    List<PetModel> findAllByIsAdopted(Boolean isAdopted);

}
