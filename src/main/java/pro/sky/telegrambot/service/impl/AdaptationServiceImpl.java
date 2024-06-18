package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.service.AdaptationService;

import java.time.LocalDate;

@Service
public class AdaptationServiceImpl implements AdaptationService {


    public AdaptationModel createAdaptation(String clientName, String petName, LocalDate finishDate) {
        return null;
    }


    @Override
    public AdaptationModel addAdaptation(AdaptationModel adaptationModel) {
        return null;
    }

    @Override
    public AdaptationModel updateAdaptation(Integer id, AdaptationModel adaptationModel) {
        return null;
    }

    @Override
    public AdaptationModel getAdaptationById(Integer id) {
        return null;
    }

    @Override
    public Integer getIdByClientIdAndPetId(Integer clientId, Integer petId) {
        return null;
    }
}
