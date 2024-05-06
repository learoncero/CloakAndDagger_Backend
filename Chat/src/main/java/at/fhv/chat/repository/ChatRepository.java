package at.fhv.chat.repository;

import at.fhv.chat.model.Chat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatRepository {
    private List<Chat> chats;

    public ChatRepository() {
        this.chats = new ArrayList<>();
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public Chat getChat(String gameCode) {
        return chats.stream()
                .filter(chat -> chat.getId().equals("Chat_" + gameCode))
                .findFirst()
                .orElse(null);
    }
}
