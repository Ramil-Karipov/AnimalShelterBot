package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.ReportModel;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportModel, Integer> {

    List<ReportModel> findAllByIsAccepted (Boolean isAccepted);
}