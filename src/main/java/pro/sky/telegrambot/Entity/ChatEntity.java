package pro.sky.telegrambot.Entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;



import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "chatentity")
public class ChatEntity {
    private final static Logger logger = LoggerFactory.getLogger(ChatEntity.class);

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private long chatId;
    private String nameUser;
    private String textChat;
    private LocalDateTime dataMessage;
    private Boolean completed;

    public ChatEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getTextChat() {
        return textChat;
    }

    public void setTextChat(String textChat) {
        this.textChat = textChat;
    }

    public LocalDateTime getDataMessage() {
        return dataMessage;
    }

    public void setDataMessage(LocalDateTime dataMessage) {
        this.dataMessage = dataMessage;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

}
