package pro.sky.telegrambot.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.impl.PetServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    @Mock
    PetRepository petRepository;
    @InjectMocks
    PetServiceImpl petService;

    PetModel testPet = new PetModel();
}
