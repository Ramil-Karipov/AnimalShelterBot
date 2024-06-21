package pro.sky.telegrambot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.AdaptationNotFoundException;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.AdaptationModel;
import pro.sky.telegrambot.repository.AdaptationRepository;
import pro.sky.telegrambot.service.AdaptationService;

import java.time.LocalDate;

@Service
public class AdaptationServiceImpl implements AdaptationService {

    private final Logger logger = LoggerFactory.getLogger(AdaptationServiceImpl.class);
    private final AdaptationRepository adaptationRepository;
    private final TelegramBotUpdatesListener listener;
    @Value("${info.extend:нет данных}")
    private String extendInfoMessage;
    @Value("${info.abort:нет данных}")
    private String abortInfoMessage;

    public AdaptationServiceImpl(AdaptationRepository adaptationRepository, TelegramBotUpdatesListener listener) {
        this.adaptationRepository = adaptationRepository;
        this.listener = listener;
    }

    public String createAdaptation(Integer petId, Integer clientId, Integer volunteerId) {

//        Тут для реализации нужны два метода для получения записей из БД по id, реализованные в PetService и
//        ClientService. В случае не нахождения сущностей в БД по указанным id, они должны выбрасывать соответствующие Exeption'ы

//        PetModel petToAdopt = petService.findPetById(petId);
//        ClientModel adoptingClient = clientService.findClientById(clientId);

//        После того как успешно получены соответствующие записи из БД, необходимо проверить, не имеет ли питомец статус
//        "на руках" и нет ли у данного клиента уже на руках какого-то другого питомца:
//        if (petToAdopt.getIsAdopted) {
//          return "Питомец с petId = " + petId + " уже находится в процессе усыновления";
//          }
//        if (adoptingClient.getPetId != null) {
//          return "У клиента с clientId = " + clientId + " уже есть на руках питомец с petId = " + adoptingClient.getPetId + " в
//          процессе адаптации";
//        }
//        Для определения даты отчета и даты окончания адаптации воспользуемся методами класса LocalDate. Для поля "дата
//        последнего отчета" при создании адаптации проставляем текущую дату. В дальнейшем она должна меняться при присвоении
//        последнему связанному отчету статуса "принят" (is_accepted = true)
        LocalDate lastReportDate = LocalDate.now();
        LocalDate finishDate = LocalDate.now().plusMonths(1);

//        AdaptationModel creatingAdaptation = new AdaptationModel(petToAdopt.getId(), adoptingClient.getId(), volunteerId,
//                lastReportDate, finishDate);
//        addAdaptation(creatingAdaptation);

//        После того как успешно завершился процесс создания адаптации для питомца и клиента, надо проапдейтить соответствующие
//                им записи в БД, насетив в них изменения по полям is_adopted и pet_id:
//        petToAdopt.setIsAdopted(true);
//        petService.updatePet(petId, petToAdopt);
//        adoptingClient.setPetId(petId);
//        clientService.updateClient(clientId, adoptingClient);
        logger.debug("Создание процесса адаптации прошло успешно.");
        return "Успешно зарегистрирован процесс адаптации питомца petId = " + petId + " клиентом clientId = " + clientId + ". Дата окончания процесса адаптации - " + finishDate + ". Ответственный волонтер: volunteerId = " + volunteerId;
    }


    @Override
    public AdaptationModel addAdaptation(AdaptationModel adaptationModel) {
        return adaptationRepository.save(adaptationModel);
    }

    @Override
    public AdaptationModel findAdaptationById(Integer id) throws AdaptationNotFoundException {
        return adaptationRepository.findById(id).orElseThrow(AdaptationNotFoundException::new);
    }

    @Override
    public AdaptationModel updateAdaptation(Integer id, AdaptationModel adaptationModel) throws AdaptationNotFoundException {
        AdaptationModel existingAdaptation = findAdaptationById(id);
        existingAdaptation.setPetId(adaptationModel.getPetId());
        existingAdaptation.setClientId(adaptationModel.getClientId());
        existingAdaptation.setVolunteerId(adaptationModel.getVolunteerId());
        existingAdaptation.setLastReportDate(adaptationModel.getLastReportDate());
        existingAdaptation.setFinishDate(adaptationModel.getFinishDate());
        existingAdaptation.setFinished(adaptationModel.getFinished());
        adaptationRepository.save(existingAdaptation);
        logger.debug("Данные для процесса адаптации с id = " + id + " успешно изменены.");
        return existingAdaptation;
    }

    @Override
    public AdaptationModel findAdaptationByPetId(Integer petId) throws AdaptationNotFoundException {
        return adaptationRepository.findAdaptationByPetId(petId).orElseThrow(() -> {
            logger.error("Для питомца с id = " + petId + " нет активного процесса адаптации.");
            return new AdaptationNotFoundException();
        });
    }

    //    Метод для продления периода адаптации на указанное количество дней. При вызове отправляет клиенту, связанному с
//    процессом адаптации информационное сообщение о продлении.
    public String extendAdaptation(Integer petId, Integer days) throws AdaptationNotFoundException {
        if (days <= 0) {
            return "Количество дней для продления должно быть положительным.";
        }
        AdaptationModel adaptationToExtend = findAdaptationByPetId(petId);
        adaptationToExtend.setFinishDate(adaptationToExtend.getFinishDate().plusDays(days));
        updateAdaptation(adaptationToExtend.getId(), adaptationToExtend);
        Integer clientId = adaptationToExtend.getClientId();

//        Здесь нужен метод для получения клиента из БД по его id и затем получения его chatId в телеграм
//        ClientModel client = clientService.findById(clientId);
//        Long chatId = client.getChatId();
//        listener.sendCustomMessage(chatId, extendInfoMessage + days + " дней.");
        return "Период адаптации питомца petId = " + petId + " успешно продлен на " + days + " дней.";
    }

    //    Метод для досрочного прерывания процесса адаптации
    public String abortAdaptation(Integer petId) throws AdaptationNotFoundException {
        AdaptationModel adaptationToAbort = findAdaptationByPetId(petId);
        adaptationToAbort.setFinished(true);
        updateAdaptation(adaptationToAbort.getId(), adaptationToAbort);
        Integer clientId = adaptationToAbort.getClientId();

//        Здесь нужен метод для получения клиента из БД по его id и затем получения его chatId в телеграм
//        ClientModel client = clientService.findById(clientId);
//        PetModel pet = petService.findByPetId(petId);
//        pet.setIsAdopted(false);
//        petService.updatePet(petId, pet);             - ставим снова false в колонке is_adopted в таблице pet
//        client.setPetId(null);
//        clientService.updateClient(clientId, client)  - ставим null в колонку pet_id в таблице client
//        Long chatId = client.getChatId();
//        listener.sendCustomMessage(chatId, abortInfoMessage);
        return "Период адаптации питомца petId = " + petId + " завершен досрочно. Необходимо забрать его у усыновителя " +
                "clientId = " + clientId + " и обеспечить его транспортировку обратно в приют.";
    }
}
