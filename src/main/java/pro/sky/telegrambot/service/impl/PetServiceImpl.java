package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.ReportNotFoundException;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.PetServicel;

@Service
public class PetServiceImpl implements PetServicel {
    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public PetModel addPet(PetModel model) {
        return petRepository.save(model);
    }

    @Override
    public PetModel updatePet(Long id, PetModel petModel) {
        if (petIsExist(id))
            return petRepository.save(petModel);

        throw new ReportNotFoundException();
    }

    private boolean petIsExist(Long id) {
        return petRepository.findById(id).isPresent();
    }
}
