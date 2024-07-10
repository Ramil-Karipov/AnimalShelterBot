package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;

public interface PetService {
    PetModel addPet(PetModel model);

    PetModel updatePet(Integer id, PetModel petModel);

    PetModel findById(Integer id);

    PetModel createPet(String name, String birthDate);

    PetModel getInfoPet();

    PetModel PetServiceImpl(PetRepository petRepository);
}
