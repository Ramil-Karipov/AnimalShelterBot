package pro.sky.telegrambot.service;

import pro.sky.telegrambot.exception.AdaptationNotFoundException;
import pro.sky.telegrambot.model.AdaptationModel;

import java.util.Optional;

public interface AdaptationService {

    AdaptationModel addAdaptation (AdaptationModel adaptationModel);

    AdaptationModel findAdaptationById(Integer id);

    AdaptationModel updateAdaptation (Integer id, AdaptationModel adaptationModel);

    AdaptationModel findAdaptationByPetId (Integer petId);
}
