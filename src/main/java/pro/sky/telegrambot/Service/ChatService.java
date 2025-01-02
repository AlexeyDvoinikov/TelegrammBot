package pro.sky.telegrambot.Service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Entity.ChatEntity;
import pro.sky.telegrambot.Repository.ChatRepository;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatEntity saveChat(ChatEntity chat){
        return chatRepository.save(chat);
    }
    public List<ChatEntity> getListChat(){
        return chatRepository.findAllByCompleted(false);
    }
    public ChatEntity findChatByIdChat(Integer chatId){
        ChatEntity findChat=chatRepository.findAllByChatId(chatId);
        //System.out.println(findChat);
        return findChat;
    }
}
