package pro.sky.telegrambot.dto;

public class ReportCreateDto {
    private Integer clientId;

    private Integer petId;

    private String petPhotoPath;

    private String petInfo;

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
}
