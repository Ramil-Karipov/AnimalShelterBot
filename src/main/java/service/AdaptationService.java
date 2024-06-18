package service;

import pro.sky.telegrambot.model.AdaptationModel;

public interface AdaptationService {

    AdaptationModel addAdaptation (AdaptationModel adaptationModel);

    AdaptationModel updateAdaptation (Integer id, AdaptationModel adaptationModel);

    AdaptationModel getAdaptationById (Integer id);

    Integer getIdByClientIdAndPetId (Integer clientId, Integer petId);
}
