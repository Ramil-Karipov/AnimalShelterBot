package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "report_date")
    private LocalDateTime reportDate;
    @Column(name = "client_id")
    private Integer clientId;
    @Column(name = "pet_id")
    private Long petId;
    @Column(name = "pet_photo_path")
    private String petPhotoPath;
    @Column(name = "pet_info")
    private String petInfo;
    @Column(name = "is_accepted")
    private boolean isAccepted;

}
