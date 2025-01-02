package pro.sky.telegrambot.Entity;

import java.time.LocalDateTime;

public class MessageDigit {
    private LocalDateTime localDateTime;
    private String messageText;

    public MessageDigit(LocalDateTime localDateTime, String messageText) {
        this.localDateTime = localDateTime;
        this.messageText = messageText;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "MessageDigit{" +
                "localDateTime=" + localDateTime +
                ", messageText='" + messageText + '\'' +
                '}';
    }
}
