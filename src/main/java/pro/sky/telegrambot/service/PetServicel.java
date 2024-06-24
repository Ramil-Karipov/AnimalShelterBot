package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.PetModel;

public interface PetServicel {
    PetModel addPet(PetModel model);

    PetModel updatePet(Long id, PetModel petModel);

}
