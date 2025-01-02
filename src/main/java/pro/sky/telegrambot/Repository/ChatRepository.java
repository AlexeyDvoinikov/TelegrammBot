package pro.sky.telegrambot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.Entity.ChatEntity;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity,Integer> {
    ChatEntity save(ChatEntity chat);
    List<ChatEntity> findAllByCompleted(Boolean completed);


    ChatEntity findAllByChatId(Integer chatId);
}
