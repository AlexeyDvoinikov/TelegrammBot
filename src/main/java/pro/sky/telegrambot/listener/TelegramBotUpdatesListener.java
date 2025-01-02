package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Entity.ChatEntity;
import pro.sky.telegrambot.Entity.MessageDigit;
import pro.sky.telegrambot.Service.ChatService;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private ChatService chatService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener() {

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null) {
                SendMessage messageNew;
                messageNew=new SendMessage(update.message().chat().id(),writeTextMessage(update))
                        .parseMode(ParseMode.Markdown);
                SendResponse response = telegramBot.execute(messageNew);
            }
            logger.info("Processing update: {}", update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public MessageDigit exstractDataTimeFromText(String text) {
        //Создаем очевидные константы для месяцев, дней, часов и минут
        final int COUNT_MONTH = 12;
        final int MAX_COUNT_DAYS_IN_MONTH = 31;
        final int MIN_COUNT_DAYS_IN_MONTH = 30;
        final int MAX_COUNT_DAYS_IN_FEBRUARY = 29;
        final int MIN_COUNT_DAYS_IN_FEBRUARY = 28;
        final int APRIL = 4;
        final int FABRUARY = 2;
        final int JUNE = 6;
        final int SEPTEMBER = 9;
        final int NOVEMBER = 11;
        final int MAX_COUNT_HOURS = 23;
        final int MAX_COUNT_MINUT = 59;
        Pattern pattern = Pattern.compile("([Н|н]апомни\\s*)(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(.*)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String textData = matcher.group(2);
            String messageText = matcher.group(3);
            //Извлечем из сообщения отдельные числа для даты, месяца и года.
            int intDD = Integer.parseInt(textData.charAt(0) + "" + textData.charAt(1) + "");
            int intMM = Integer.parseInt(textData.charAt(3) + "" + textData.charAt(4) + "");
            int intGGGG = Integer.parseInt(textData.charAt(6) + "" + textData.charAt(7) + ""
                    + textData.charAt(8) + "" + textData.charAt(9) + "");
            //Извлечем из сообщения отдельные числа для часов и минут.
            int intHH = Integer.parseInt(textData.charAt(11) + "" + textData.charAt(12) + "");
            int intMm = Integer.parseInt(textData.charAt(14) + "" + textData.charAt(15) + "");
            //Проверяем, что номер масяца не более 12 и кол-во дней в месяце не более 31
            if (intMM <= COUNT_MONTH && intDD <= MAX_COUNT_DAYS_IN_MONTH) {
                //Проверяем, что в определенных месяцах кол-во дней не более 30
                if (((intMM == APRIL | intMM == JUNE | intMM == SEPTEMBER | intMM == NOVEMBER) & intDD > MIN_COUNT_DAYS_IN_MONTH) |
                        //В феврале весокосном не более 29 дней, а в простом не более 28
                        (intMM == FABRUARY & (intGGGG % 4 != 0 && intDD > MIN_COUNT_DAYS_IN_FEBRUARY) |
                                (intGGGG % 4 == 0 && intDD > MAX_COUNT_DAYS_IN_FEBRUARY))) return null;
                //Проверяем корректность часов и минут
                if (intHH <= MAX_COUNT_HOURS && intMm <= MAX_COUNT_MINUT)
                    return new MessageDigit(LocalDateTime.parse(matcher.group(2), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                            , matcher.group(3));

            }

        }

        return null;
    }
    private String writeTextMessage(Update update){
        String textSendMessage;
        String textMessage=update.message().text();
        if (textMessage.equals("/start")) {
            textSendMessage = "Привет, " + update.message().from().firstName()
                    + ". Рад познакомиться! Меня зовут Юстас.\n" +
                    "Я могу помочь вам напомнить о какой-то задаче.\n" +
                    "Укажите дату, время и саму задачу в формате: \n \"" +
                    "дд.мм.гггг чч:мм текст сообщения\"";
        } else if (textMessage.contains("апомни")) {
            if (exstractDataTimeFromText(update.message().text()) != null) {
                String message = "";
                message = exstractDataTimeFromText(update.message().text()).getMessageText();
               textSendMessage= " Я напомню Вам " + exstractDataTimeFromText(update.message().text()).getLocalDateTime() +
                        " " + message;
                ChatEntity request = new ChatEntity();
                request.setDataMessage(exstractDataTimeFromText(update.message().text())
                        .getLocalDateTime());
                request.setChatId(update.message().chat().id());
                request.setTextChat(message);
                request.setCompleted(false);
                request.setNameUser(update.message().from().firstName());
                chatService.saveChat(request);
            } else {
               textSendMessage= update.message().from().firstName() +
                        ", введите, пожалуйста, команду в формате:\n" +
                        "Напомни дд.мм.гггг чч:мм текст";
            }
        } else {
           textSendMessage=
                    update.message().from().firstName() + ", я могу Вам напомнить о какой-то задаче.\n" +
                            "Укажите дату, время и текс самой задачу в формате: \n\n" + "\"дд.мм.гггг чч:мм текст задачи\"\n\n" +
                            " без кавычек. \n Например:\n\n Напомни 12.12.2222 15:02 прогуляться по улицам Марса\n\n" +
                            " Для других задач я пока не предназначен";
        }
        return textSendMessage;
    }


}
