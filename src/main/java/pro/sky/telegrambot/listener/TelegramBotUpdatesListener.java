package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.model.PetModel;
import pro.sky.telegrambot.model.VolunteerModel;
import pro.sky.telegrambot.service.ClientService;
import pro.sky.telegrambot.service.PetService;
import pro.sky.telegrambot.service.impl.PhoneNumberValidatorImpl;
import pro.sky.telegrambot.service.impl.VolunteerServiceImpl;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private TelegramBotConfiguration configuration;
    @Autowired
    private VolunteerServiceImpl volunteerService;
    @Autowired
    private PetService petService;
    @Autowired
    private PhoneNumberValidatorImpl phoneNumberValidator;
    @Autowired
    private ClientService clientService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                logger.info("Processing update: {}", update);

                //Здесь объявлены переменные для сущности сообщения message, для хранения извлеченного содержания текста text,
                // для хранения идентификатора чата, с которого отправили сообщение chatId
                Message message = update.message();
                String text;
                Long chatId;

                //Здесь извлекаем нужные нам данные либо из message (если команда отправлялась с клавиатуры), либо из callbackQuery (если
                //команда отправлялась кнопкой меню и в message у нас null
                if (message != null) {
                    text = message.text();
                    chatId = message.chat().id();
                } else if (update.callbackQuery() != null) {
                    text = update.callbackQuery().data();
                    chatId = update.callbackQuery().from().id();
                } else {
                    return;
                }

                //Общая переменная для отправки сообщений пользователю, которая будет переопределяться в зависимости от
                //содержания отправляемых сообщений
                SendMessage send;
                //Общая переменная для отправки в чат ботом картинок
                SendPhoto photo = null;

                //Обрабатываем команду /start
                if (text.equalsIgnoreCase("/start") && message != null) {
                    send = new SendMessage(chatId, "Привет, " + message.from().firstName() + "! Это чат-бот приюта домашних животных." +
                            "Для получения списка доступных команд, введи команду '/menu'");
                    telegramBot.execute(send);

                    //Строим меню из кнопок по команде /menu
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
                    //Проверяет, является ли текст сообщения валидным. В случае успеха выводит сообщение о регистрации.
                } else if (message != null && PhoneNumberValidatorImpl.isValid(message.text())) {
                    if (phoneNumberValidator.isUserExist(message.text())) {
                        send = new SendMessage(chatId, "Такой номер уже зарегестрирован.");
                        telegramBot.execute(send);
                    } else {
                        PhoneNumberValidatorImpl.saveClient(message.text(), chatId, clientService);
                        send = new SendMessage(chatId, "Регистрация прошла успешно!");
                        telegramBot.execute(send);
                    }

                } else {
                    //Основной блок обработки команд. По мере реализации методов командой будем их вставлять сюда
                    switch (text) {
                        case ("/registration"):
                            send = new SendMessage(chatId, "Для регистрации в базе усыновителей нашего приюта, пришлите" +
                                    " сообщение, содержащее ваш номер телефона и ваше имя в формате:\n +7-9**-***-**-** - Ваше имя" +
                                    "\n +79********* - Ваше имя" + "\n 8-9**-***-**-** - Ваше имя" + "\n 8********** - Ваше имя");
                            break;
                        case ("/shelterinfo"):
                            send = new SendMessage(chatId, configuration.getShelter());
                            break;
                        case ("/howtoget"):
                            send = new SendMessage(chatId, "Схема проезда до нашего приюта:");
                            photo = new SendPhoto(chatId, new File(configuration.getHowToGetImagePath()));
                            break;
                        case ("/securityinfo"):
                            send = new SendMessage(chatId, configuration.getSecurity());
                            break;
                        case ("/safetyinfo"):
                            send = new SendMessage(chatId, configuration.getSafety());
                            break;
                        case ("/pets"):
                            List<PetModel> pets = petService.findAllByIsAdopted(false);
                            StringBuilder response = new StringBuilder();
                            for (PetModel pet : pets) {
                                response.append(pet.getInfoPet()).append("\n");
                            }
                            send = new SendMessage(chatId, response.toString());
                            break;
                        case ("/documents"):
                            send = new SendMessage(chatId, configuration.getDocuments());
                            break;
                        case ("/makeacquaintance"):
                            send = new SendMessage(chatId, configuration.getAcquaintance());
                            break;
                        case ("/howtotransport"):
                            send = new SendMessage(chatId, configuration.getToTransport());
                            break;
                        case ("/reportform"):
                            send = new SendMessage(chatId, configuration.getReportInfo());
                            photo = new SendPhoto(chatId, new File(configuration.getReportFormImagePath()));
                            break;
                        case ("/homeforpuppy"):
                            send = new SendMessage(chatId, configuration.getPuppy());
                            break;
                        case ("/homeforadultdog"):
                            send = new SendMessage(chatId, configuration.getAdultDog());
                            break;
                        case ("/homeforlimopportunities"):
                            send = new SendMessage(chatId, configuration.getLimopport());
                            break;
                        case ("/doghandlers"):
                            send = new SendMessage(chatId, configuration.getDogHandlers());
                            break;
                        case ("/doghandlersadvices"):
                            send = new SendMessage(chatId, configuration.getDogHandlersAdvices());
                            break;
                        case ("/resonsforrefuse"):
                            send = new SendMessage(chatId, configuration.getRefuse());
                            break;
                        case ("/volonteerscontacts"):
                            VolunteerModel volunteerToContact = volunteerService.getRandomVolunteer();
                            send = new SendMessage(chatId, "На все ваши вопросы всегда рад ответить наш волонтер:\n" +
                                    volunteerToContact.getInfo());
                            break;
                        default:
                            send = new SendMessage(chatId, "Command not supported");
                    }
                    telegramBot.execute(send);
                    if (photo != null) {
                        telegramBot.execute(photo);
                    }
                }
            } catch (Exception e) {
                //Ловим и обрабатываем любые исключения в процессе выполнения метода process()
                //для гарантии достижения строчки с return, чтобы бот не падал в случае
                //нештатной ситуации и сохранял активность
                logger.error("Failed to process {}", update, e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendCustomMessage(Long chatId, String text) {
        SendMessage messageToSend = new SendMessage(chatId, text);
        telegramBot.execute(messageToSend);
    }

}
