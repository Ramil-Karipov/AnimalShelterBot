package pro.sky.telegrambot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReportUpdateDto {
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    private Integer clientId;

    private Integer petId;

    private String petPhotoPath;

    private String petInfo;

    private Boolean isAccepted;

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
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

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
