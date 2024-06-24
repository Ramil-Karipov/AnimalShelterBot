package pro.sky.telegrambot.model;

import javax.persistence.*;

@Entity
@Table(name = "pet")

public class PetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "pet_view")
    private String petView;
    @Column(name = "pet_floor")
    private String floor;
    @Column(name = "pet_nickname")
    private String nickname;
    @Column(name = "pet_breed")
    private String breed;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getPetView() {
        return petView;
    }

    public void setPetView(String petView) {
        this.petView = petView;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
}
