package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс, представляющий сущность "Процесс адаптации"
 */
@Entity
@Table(name = "adaptation")
public class AdaptationModel {
    /**
     * Идентификатор записи в БД, определяющий конкретный процесс адаптации. Генерируется самой БД при создании новой записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Идентификатор питомца, проходящего процесс адаптации. Соответствует значению поля id из таблицы pet.
     */
    @Column(name = "pet_id")
    private Integer petId;
    /**
     * Идентификатор клиента, связанного с процессом адаптации. Соответствует значению поля id из таблицы client.
     */
    @Column(name = "client_id")
    private Integer clientId;
    /**
     * Идентификатор волонтера, назначенного ответственным за процесс адаптации. Соответствует значению поля id из таблицы volunteer.
     */
    @Column(name = "volunteer_id")
    private Integer volunteerId;
    /**
     * Дата, соответствующая последнему отчету о прохождении питомцем адаптации, отправленному клиентом на данный момент.
     */
    @Column(name = "last_report_date")
    private LocalDate lastReportDate;
    /**
     * Предполагаемая дата завершения процесса адаптации.
     */
    @Column(name = "finish_date")
    private LocalDate finishDate;
    /**
     * Статус процесса адаптации - завершен или нет.
     */
    @Column(name = "is_finished")
    private Boolean isFinished = false;

    public AdaptationModel(Integer petId, Integer clientId, Integer volunteerId, LocalDate lastReportDate, LocalDate finishDate) {
        this.petId = petId;
        this.clientId = clientId;
        this.volunteerId = volunteerId;
        this.lastReportDate = lastReportDate;
        this.finishDate = finishDate;
    }

    public AdaptationModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Integer volunteerId) {
        this.volunteerId = volunteerId;
    }

    public LocalDate getLastReportDate() {
        return lastReportDate;
    }

    public void setLastReportDate(LocalDate lastReportDate) {
        this.lastReportDate = lastReportDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdaptationModel that = (AdaptationModel) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
