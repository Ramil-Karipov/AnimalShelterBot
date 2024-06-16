package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Message message = update.message();
            String text;
            Long chatId;
            String userFirstName;

            if (message != null) {
                text = message.text();
                chatId = message.chat().id();
            } else if (update.callbackQuery() != null) {
                text = update.callbackQuery().data();
                chatId = update.callbackQuery().from().id();
            } else {
                return;
            }

            SendMessage send;

            if (text.equalsIgnoreCase("/start") && message != null) {
                send = new SendMessage(chatId, "Привет, " + message.from().firstName() + "! Это чат-бот приюта домашних животных." +
                        "Для получения списка доступных команд, введи команду '/menu'" +
                        " или воспользуйся кнопкой в левом нижнем углу.");
                telegramBot.execute(send);
            } else if (text.equalsIgnoreCase("/menu")) {
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.addRow(
                        new InlineKeyboardButton("Регистрация").callbackData("/registration"));
                markup.addRow(
                        new InlineKeyboardButton("О приюте").callbackData("/shelterinfo"),
                        new InlineKeyboardButton("Добраться").callbackData("/howtoget"),
                        new InlineKeyboardButton("Пропуск").callbackData("/securityinfo"),
                        new InlineKeyboardButton("Правила").callbackData("/safetyinfo"));
                markup.addRow(
                        new InlineKeyboardButton("Питомцы").callbackData("/pets"),
                        new InlineKeyboardButton("Документы").callbackData("/documents"),
                        new InlineKeyboardButton("Знакомство").callbackData("/makeacquaintance"),
                        new InlineKeyboardButton("Перевозка").callbackData("/howtotransport"));
                markup.addRow(
                        new InlineKeyboardButton("Отчет").callbackData("/reportform"),
                        new InlineKeyboardButton("Для щенка").callbackData("/homeforpuppy"),
                        new InlineKeyboardButton("Для собаки").callbackData("/homeforadultdog"),
                        new InlineKeyboardButton("Спец. дом").callbackData("/homeforlimopportunities"));
                markup.addRow(
                        new InlineKeyboardButton("Кинологи").callbackData("/doghandlers"),
                        new InlineKeyboardButton("Советы кинологов").callbackData("/doghandlersadvices"),
                        new InlineKeyboardButton("Причины отказа").callbackData("/resonsforrefuse"));
                markup.addRow(
                        new InlineKeyboardButton("Связаться с нашими волонтерами").callbackData("/volonteerscontacts"));
                send = new SendMessage(chatId, "Главное меню бота. Выбери функцию:").replyMarkup(markup);
                telegramBot.execute(send);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
