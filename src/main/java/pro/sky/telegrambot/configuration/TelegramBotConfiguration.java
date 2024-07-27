package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;
    @Value("${info.shelter:нет данных}")
    private String shelter;
    @Value("${info.security:нет данных}")
    private String security;
    @Value("${info.safety:нет данных}")
    private String safety;
    @Value("${info.acquaintance:нет данных}")
    private String acquaintance;
    @Value("${info.documents:нет данных}")
    private String documents;
    @Value("${info.totransport:нет данных}")
    private String toTransport;
    @Value("${info.puppy:нет данных}")
    private String puppy;
    @Value("${info.adultdog:нет данных}")
    private String adultDog;
    @Value("${info.limopport:нет данных}")
    private String limopport;
    @Value("${info.doghandlers:нет данных}")
    private String dogHandlers;
    @Value("${info.dhadvices:нет данных}")
    private String dogHandlersAdvices;
    @Value("${info.refuse:нет данных}")
    private String refuse;
    @Value("${howToGet.image.path:нет данных}")
    private String howToGetImagePath;
    @Value("${reportForm.image.path:нет данных}")
    private String reportFormImagePath;
    @Value("${info.report:нет данных}")
    private String reportInfo;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

}
