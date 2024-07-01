package pro.sky.telegrambot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name="client")
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_name", nullable = false)
    private String name;

    @Column(name = "client_phone", nullable = false)
    private String phone;

    @Column(name = "client_chat_id")
    private Long chat_id ;

    @Column(name = "pet_id")
    private Integer pet_id ;

    public ClientModel(){
    };




}
