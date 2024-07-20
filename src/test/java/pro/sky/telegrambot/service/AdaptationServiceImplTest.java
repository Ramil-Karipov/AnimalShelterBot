package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.exception.ClientNotFoundException;
import pro.sky.telegrambot.exception.PetNotFoundException;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.model.VolunteerModel;
import pro.sky.telegrambot.repository.AdaptationRepository;
import pro.sky.telegrambot.service.impl.AdaptationServiceImpl;
import pro.sky.telegrambot.service.impl.ClientServiceImpl;
import pro.sky.telegrambot.service.impl.PetServiceImpl;
import pro.sky.telegrambot.service.impl.VolunteerServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdaptationServiceImplTest {
    @Mock
    AdaptationRepository adaptationRepository;
    @Mock
    VolunteerServiceImpl volunteerService;
    @Mock
    PetServiceImpl petService;
    @Mock
    ClientServiceImpl clientService;
    @Mock
    TelegramBotUpdatesListener listener;
    @InjectMocks
    AdaptationServiceImpl adaptationService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    ClientModel testClientWithPet;
    ClientModel testClientWithoutPet;
    PetModel testPetAdopted;
    PetModel testPetNotAdopted;
    VolunteerModel testVolunteer;
    AdaptationModel testAdaptation;

    @BeforeEach
    void init() {
        testClientWithPet = new ClientModel();
        testClientWithPet.setId(1);
        testClientWithPet.setName("ClientWithPet");
        testClientWithPet.setPhone("+7-999-999-66-66");
        testClientWithPet.setChatId(777777L);
        testClientWithPet.setPetId(1);

        testClientWithoutPet = new ClientModel();
        testClientWithoutPet.setId(2);
        testClientWithoutPet.setName("ClientWithoutPet");
        testClientWithoutPet.setPhone("+7-999-999-66-66");
        testClientWithoutPet.setChatId(777777L);

        testPetAdopted = new PetModel();
        testPetAdopted.setPetId(1);
        testPetAdopted.setName("PetAdopted");
        testPetAdopted.setBirthDate(LocalDate.parse("01.01.2020", formatter));
        testPetAdopted.setAdopted(true);

        testPetNotAdopted = new PetModel();
        testPetNotAdopted.setPetId(2);
        testPetNotAdopted.setName("PetNotAdopted");
        testPetNotAdopted.setBirthDate(LocalDate.parse("02.02.2020", formatter));

        testVolunteer = new VolunteerModel();
        testVolunteer.setId(2);
        testVolunteer.setName("TestVolunteer");
        testVolunteer.setChatId(555666L);
        testVolunteer.setTelegramURL("Telegram_url");

        testAdaptation = new AdaptationModel(1, 1, 2, LocalDate.now(),
                LocalDate.now().plusMonths(1));
        testAdaptation.setId(1);
    }

    @Test
    void createAdaptationPositive() {
        when(petService.findById(2)).thenReturn(testPetNotAdopted);
        when(clientService.getClient(2)).thenReturn(Optional.of(testClientWithoutPet));
        when(volunteerService.findVolunteerById(2)).thenReturn(testVolunteer);
        AdaptationModel expectedAdaptation = new AdaptationModel(2, 2, 2, LocalDate.now(),
                LocalDate.now().plusMonths(1));
        expectedAdaptation.setId(1);
        AdaptationModel actualAdaptation = adaptationService.createAdaptation(2, 2, 2);
        actualAdaptation.setId(1);
        assertEquals(expectedAdaptation, actualAdaptation);
        assertTrue(testPetNotAdopted.getAdopted());
        assertEquals(2, testClientWithoutPet.getPetId());
    }

    @Test
    void createAdaptationNegativePetIsAdopted() {
        when(petService.findById(1)).thenReturn(testPetAdopted);
        when(clientService.getClient(2)).thenReturn(Optional.of(testClientWithoutPet));
        assertThrows(RuntimeException.class, () ->
            adaptationService.createAdaptation(1, 2, 2)
        , "Питомец с petId = 1 уже находится в процессе усыновления");
    }

    @Test
    void createAdaptationNegativeClientHasPet() {
        when(petService.findById(2)).thenReturn(testPetNotAdopted);
        when(clientService.getClient(1)).thenReturn(Optional.of(testClientWithPet));
        assertThrows(RuntimeException.class, () ->
            adaptationService.createAdaptation(2, 1, 2)
        , "У клиента с clientId = 1 уже есть на руках питомец с petId = 1 в процессе адаптации");
    }

    @Test
    void createAdaptationNegativePetNotFound() {
        when(petService.findById(0)).thenThrow(PetNotFoundException.class);
        assertThrows(PetNotFoundException.class, () ->
            adaptationService.createAdaptation(0, 2, 2)
        , "Питомец не найден");
    }

    @Test
    void createAdaptationNegativeClientNotFound() {
        when(clientService.getClient(0)).thenThrow(ClientNotFoundException.class);
        assertThrows(ClientNotFoundException.class, () ->
            adaptationService.createAdaptation(1, 0, 2)
        , "Client not found.");
    }

    @Test
    void extendAdaptationPositive() {
        when(adaptationRepository.findAdaptationByPetId(1)).thenReturn(Optional.of(testAdaptation));
        when(adaptationRepository.findById(1)).thenReturn(Optional.of(testAdaptation));
        when(clientService.getClient(1)).thenReturn(Optional.of(testClientWithPet));
        AdaptationModel extendedAdaptation = adaptationService.extendAdaptation(1, 10);
        assertEquals(LocalDate.now().plusMonths(1).plusDays(10), extendedAdaptation.getFinishDate());
    }

    @Test
    void extendAdaptationNegative() {
        assertThrows(RuntimeException.class, () ->
            adaptationService.extendAdaptation(1, 0), "Invalid days value");
    }

    @Test
    void abortAdaptation() {
        when(adaptationRepository.findAdaptationByPetId(1)).thenReturn(Optional.of(testAdaptation));
        when(adaptationRepository.findById(1)).thenReturn(Optional.of(testAdaptation));
        when(clientService.getClient(1)).thenReturn(Optional.of(testClientWithPet));
        when(petService.findById(1)).thenReturn(testPetAdopted);
        when(volunteerService.findVolunteerById(2)).thenReturn(testVolunteer);
        adaptationService.abortAdaptation(1);
        assertTrue(testAdaptation.getFinished());
        assertFalse(testPetAdopted.getAdopted());
        assertNull(testClientWithPet.getPetId());
    }
}
