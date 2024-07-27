package pro.sky.telegrambot.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.PetNotFoundException;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.PetService;

import java.time.LocalDate;
import java.util.List;

import static pro.sky.telegrambot.model.PetModel.formatter;

@Service
public class PetServiceImpl implements PetService {

    private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

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

    public PetModel createPet(String name, String birthDate) throws IllegalArgumentException {
        if (StringUtils.isEmpty(name) || StringUtils.isBlank(name) || !StringUtils.isAlpha(name)) {
            logger.error("Invalid name input");
            throw new IllegalArgumentException("Invalid name input");
        }
        if (!birthDate.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
            logger.error("Invalid date format");
            throw new IllegalArgumentException("Invalid date format");
        }
            PetModel creatingPet = new PetModel();
            LocalDate birthDay = LocalDate.parse(birthDate, formatter);
            creatingPet.setName(name);
            creatingPet.setBirthDate(birthDay);
            addPet(creatingPet);
            return creatingPet;
    }

    public List<PetModel> findAllByIsAdopted(Boolean isAdopted) {
        return petRepository.findAllByIsAdopted(isAdopted);
    }



}

