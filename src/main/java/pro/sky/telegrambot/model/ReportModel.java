package pro.sky.telegrambot.model;

import javax.persistence.*;

@Entity
@Table(name = "report")
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "report_date")
    private String reportDate;
    @Column(name = "client_id")
    private int clientId;
    @Column(name = "pet_id")
    private Long petId;
    @Column(name = "pet_photo_path")
    private String petPhotoPath;
    @Column(name = "pet_info")
    private String petInfo;
    @Column(name = "is_accepted")
    private boolean isAccepted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetPhotoPath() {
        return petPhotoPath;
    }

    public void setPetPhotoPath(String petPhotoPath) {
        this.petPhotoPath = petPhotoPath;
    }

    public String getPetInfo() {
        return petInfo;
    }

    public void setPetInfo(String petInfo) {
        this.petInfo = petInfo;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
