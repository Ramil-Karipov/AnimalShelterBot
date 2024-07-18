package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.PetModel;

import java.util.List;

public interface PetService {
    PetModel addPet(PetModel model);

    PetModel updatePet(Integer id, PetModel petModel);

    PetModel findById(Integer id);

    PetModel createPet(String name, String birthDate);

    List<PetModel> findAllByIsAdopted(Boolean isAdopted);

}
