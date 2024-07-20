package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.AdaptationModel;

public interface AdaptationService {

    AdaptationModel createAdaptation(Integer petId, Integer clientId, Integer volunteerId);

    AdaptationModel addAdaptation (AdaptationModel adaptationModel);

    AdaptationModel findAdaptationById(Integer id);

    AdaptationModel updateAdaptation (Integer id, AdaptationModel adaptationModel);

    AdaptationModel findAdaptationByPetId (Integer petId);

    AdaptationModel extendAdaptation(Integer petId, Integer days);

    void abortAdaptation(Integer petId);
}
