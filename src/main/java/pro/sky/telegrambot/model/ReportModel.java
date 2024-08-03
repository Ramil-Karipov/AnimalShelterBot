package pro.sky.telegrambot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "report")
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "pet_id")
    private Integer petId;

    @Column(name = "pet_photo_path")
    private String petPhotoPath;

    @Column(name = "pet_info")
    private String petInfo;

    @Column(name = "is_accepted")
    private Boolean isAccepted = false;

    public ReportModel() {
    }

    public ReportModel(Integer id, LocalDate reportDate, Integer clientId, Integer petId, String petPhotoPath, String petInfo, boolean isAccepted) {
        this.id = id;
        this.reportDate = reportDate;
        this.clientId = clientId;
        this.petId = petId;
        this.petPhotoPath = petPhotoPath;
        this.petInfo = petInfo;
        this.isAccepted = isAccepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportModel that = (ReportModel) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getReportDate(), that.getReportDate()) &&
                Objects.equals(getClientId(), that.getClientId()) &&
                Objects.equals(getPetId(), that.getPetId()) &&
                Objects.equals(getPetPhotoPath(), that.getPetPhotoPath()) &&
                Objects.equals(getPetInfo(), that.getPetInfo()) &&
                Objects.equals(getIsAccepted(), that.getIsAccepted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getReportDate(), getClientId(), getPetId(), getPetPhotoPath(), getPetInfo(), getIsAccepted());
    }
}
