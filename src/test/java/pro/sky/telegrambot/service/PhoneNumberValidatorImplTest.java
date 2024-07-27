package pro.sky.telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.ClientModel;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.impl.PhoneNumberValidatorImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PhoneNumberValidatorImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private PhoneNumberValidatorImpl phoneNumberValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsValid() {
        assertTrue(PhoneNumberValidatorImpl.isValid("+7-912-345-67-89 - John Doe"));
        assertTrue(PhoneNumberValidatorImpl.isValid("8-912-345-67-89 - John Doe"));
        assertTrue(PhoneNumberValidatorImpl.isValid("89123456789 - John Doe"));
        assertTrue(PhoneNumberValidatorImpl.isValid("+79123456789 - John Doe"));
        assertTrue(PhoneNumberValidatorImpl.isValid("79123456789 - John Doe"));
        assertFalse(PhoneNumberValidatorImpl.isValid("123-456-789 - John Doe"));
        assertFalse(PhoneNumberValidatorImpl.isValid("+79123456789 John Doe"));
    }

    @Test
    public void testFormatPhoneNumber() {
        assertEquals("+7-912-345-67-89", PhoneNumberValidatorImpl.formatPhoneNumber("79123456789"));
        assertEquals("+7-912-345-67-89", PhoneNumberValidatorImpl.formatPhoneNumber("89123456789"));
        assertThrows(IllegalArgumentException.class, () -> {
            PhoneNumberValidatorImpl.formatPhoneNumber("123456789");
        });
    }

    @Test
    public void testSaveClient() {
        String message = "+7-912-345-67-89 - John Doe";
        Long chatId = 123456789L;
        ClientModel clientModel = new ClientModel();
        clientModel.setPhone("+7-912-345-67-89");
        clientModel.setName("John Doe");
        clientModel.setChatId(chatId);

        when(clientService.createClient(any(ClientModel.class))).thenReturn(clientModel);

        ClientModel savedClient = PhoneNumberValidatorImpl.saveClient(message, chatId, clientService);

        verify(clientService, times(1)).createClient(any(ClientModel.class));
        assertEquals("+7-912-345-67-89", savedClient.getPhone());
        assertEquals("John Doe", savedClient.getName());
        assertEquals(chatId, savedClient.getChatId());
    }

    @Test
    public void testIsUserExist() {
        String message = "+7-912-345-67-89 - John Doe";
        String formattedPhoneNumber = "+7-912-345-67-89";

        when(clientRepository.findByPhone(formattedPhoneNumber)).thenReturn(Optional.of(new ClientModel()));

        boolean userExists = phoneNumberValidator.isUserExist(message);

        verify(clientRepository, times(1)).findByPhone(formattedPhoneNumber);
        assertTrue(userExists);
    }

    @Test
    public void testIsUserNotExist() {
        String message = "+7-912-345-67-89 - John Doe";
        String formattedPhoneNumber = "+7-912-345-67-89";

        when(clientRepository.findByPhone(formattedPhoneNumber)).thenReturn(Optional.empty());

        boolean userExists = phoneNumberValidator.isUserExist(message);

        verify(clientRepository, times(1)).findByPhone(formattedPhoneNumber);
        assertFalse(userExists);
    }
}

