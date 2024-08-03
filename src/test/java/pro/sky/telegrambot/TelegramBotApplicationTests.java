package pro.sky.telegrambot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.telegrambot.controller.AdaptationController;
import pro.sky.telegrambot.controller.ClientController;
import pro.sky.telegrambot.controller.PetController;
import pro.sky.telegrambot.controller.ReportController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelegramBotApplicationTests {

    @Autowired
    AdaptationController adaptationController;
    @Autowired
    ClientController clientController;
    @Autowired
    PetController petController;
    @Autowired
    ReportController reportController;

    @Test
    void contextLoads() {
        assertNotNull(adaptationController);
        assertNotNull(clientController);
        assertNotNull(petController);
        assertNotNull(reportController);
    }

}
