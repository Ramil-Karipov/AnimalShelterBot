package pro.sky.telegrambot.model;

import javax.persistence.*;

@Entity
@Table(name = "volunteer")
public class VolunteerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "name")
    String name;
    @Column(name = "tg_url")
    String telegramURL;

    public VolunteerModel() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelegramURL() {
        return telegramURL;
    }

    public void setTelegramURL(String telegramURL) {
        this.telegramURL = telegramURL;
    }

    public String getInfo() {
        return "Имя: " + this.getName() + "\n" + "Ссылка на чат в Telegram: " + this.getTelegramURL();
    }
}
