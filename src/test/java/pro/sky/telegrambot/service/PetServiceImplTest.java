package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.exception.PetNotFoundException;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.impl.PetServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    PetRepository petRepository;
    @InjectMocks
    PetServiceImpl petService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    PetModel testPet = new PetModel();

    @BeforeEach
    void setUp() {
        testPet.setId(1);
        testPet.setName("testPet");
        testPet.setBirthDate(LocalDate.parse("01.01.1111", formatter));
    }

    @Test
    void createPetPositive() {
        when(petRepository.save(any())).thenReturn(testPet);
        PetModel actualPet = petService.createPet("testPet", "01.01.1111");
        assertNotNull(actualPet);
        actualPet.setId(1);
        assertEquals(testPet.getId(), actualPet.getId());
        assertEquals(testPet.getName(), actualPet.getName());
        assertEquals(testPet.getBirthDate(), actualPet.getBirthDate());
    }

    @Test
    void createPetNegativeInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                petService.createPet(" ", "01.01.1111"));
    }
    @Test
    void createPetNegativeInvalidBirthDate() {
        assertThrows(IllegalArgumentException.class, () ->
                petService.createPet("testPet", "01-01-1111"));
    }

    @Test
    void findByIdPositive() {
        when(petRepository.findById(1)).thenReturn(Optional.of(testPet));
        PetModel actualPet = petService.findById(1);
        assertNotNull(actualPet);
        assertEquals(testPet.getId(), actualPet.getId());
        assertEquals(testPet.getName(), actualPet.getName());
        assertEquals(testPet.getBirthDate(), actualPet.getBirthDate());
    }

    @Test
    void findByIdNegative() {
        assertThrows(PetNotFoundException.class, () ->
                petService.findById(2));
    }

    @Test
    void updatePetPositive() {
        when(petRepository.findById(1)).thenReturn(Optional.of(testPet));
        when(petRepository.save(any())).thenReturn(testPet);
        PetModel updatedPet = petService.updatePet(1, testPet);
        assertNotNull(updatedPet);
        assertEquals(testPet.getId(), updatedPet.getId());
        assertEquals(testPet.getName(), updatedPet.getName());
        assertEquals(testPet.getBirthDate(), updatedPet.getBirthDate());
    }

    @Test
    void updatePetNegative() {
        assertThrows(PetNotFoundException.class, () ->
                petService.updatePet(2, testPet));
    }
}
