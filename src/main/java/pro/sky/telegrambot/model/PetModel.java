package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pet")

public class PetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer petId;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "name")
    private String name;
    @Column(name = "is_adopted")
    private Boolean isAdopted = false;

    public PetModel() {
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdopted() {
        return isAdopted;
    }

    public void setAdopted(Boolean adopted) {
        isAdopted = adopted;
    }

    public String getInfoPet() {
        return "Кличка: " + this.getName() + "  День рождение питомца: " + this.getBirthDate() + "\n";
    }
}
