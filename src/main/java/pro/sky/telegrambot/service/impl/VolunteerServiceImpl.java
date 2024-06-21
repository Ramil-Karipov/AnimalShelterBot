package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.VolunteerModel;
import pro.sky.telegrambot.repository.VolunteerRepository;

@Service
public class VolunteerServiceImpl {

    private final VolunteerRepository repository;

    public VolunteerServiceImpl(VolunteerRepository repository) {
        this.repository = repository;
    }

    public VolunteerModel getRandomVolunteer() {
        return repository.getRandomVolunteer();
    }
}
