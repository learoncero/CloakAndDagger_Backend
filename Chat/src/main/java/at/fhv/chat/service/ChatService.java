package at.fhv.chat.service;

import at.fhv.chat.model.Chat;
import at.fhv.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat startChat(String gameCode) {
        Chat chat = new Chat(gameCode);
        chatRepository.addChat(chat);

        return chat;
    }

    public Chat sendMessage(String gameCode, String message, String sender) {
        Chat chat = chatRepository.getChat(gameCode);
        chat.addMessage(message, sender);

        return chat;
    }
}
