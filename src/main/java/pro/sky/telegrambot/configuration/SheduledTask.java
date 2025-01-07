package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.Entity.ChatEntity;
import pro.sky.telegrambot.Service.ChatService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SheduledTask {
    private final static Logger logger = LoggerFactory.getLogger(SheduledTask.class);
    @Autowired
    private ChatService chatService;

    @Autowired
    private TelegramBot telegramBot;
    SendMessage message = null;
    Boolean dateTimeX = false;

    List<ChatEntity> chatEntityList;

    @Scheduled(fixedRate = 10000) // вызов метода каждые 10000ms (10 секунд)
    public void performTask() {
        chatEntityList = chatService.getListChat();
        for (ChatEntity chat : chatEntityList) {
            dateTimeX = LocalDateTime.now().isAfter(chat.getDataMessage());
            if (dateTimeX) {
                message = new SendMessage(chat.getChatId()
                        , chat.getNameUser() + ", Вы просили меня напомнить Вам "
                        + chat.getTextChat()).parseMode(ParseMode.Markdown);
                SendResponse response = telegramBot.execute(message);
                chat.setCompleted(true);
                logger.info("Задача Shduller: " + chat.getChatId() + " " + chat.getNameUser() + " " + chat.getTextChat());
                chatService.saveChat(chat);
            }

        }
    }

}
