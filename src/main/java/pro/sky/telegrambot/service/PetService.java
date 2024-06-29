package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.PetModel;

public interface PetService {
    PetModel addPet(PetModel model);

    PetModel updatePet(Integer id, PetModel petModel);

    PetModel findById(Integer id);

}
