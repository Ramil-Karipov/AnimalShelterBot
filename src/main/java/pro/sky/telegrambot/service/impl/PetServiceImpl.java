package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.PetNotFoundException;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.PetService;

@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public PetModel addPet(PetModel model) {
        return petRepository.save(model);
    }

    @Override
    public PetModel updatePet(Integer id, PetModel petModel) {
        if (petIsExist(id))
            return petRepository.save(petModel);

        throw new PetNotFoundException();
    }

    private boolean petIsExist(Integer id) {
        return petRepository.findById(id).isPresent();
    }

    public PetModel findById(Integer id) {
        return petRepository.findById(id).orElseThrow(PetNotFoundException::new);
    }
}
