package at.fhv.chat.service;

import at.fhv.chat.model.Chat;
import at.fhv.chat.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatServiceTest {

    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        chatService = new ChatService(chatRepository);
    }

    @Test
    public void testSendMessage() {
        String gameCode = "ABC123";
        String message = "Hello!";
        String sender = "User";

        Chat chat = new Chat(gameCode);

        when(chatRepository.getChat(gameCode)).thenReturn(chat);

        Chat updatedChat = chatService.sendMessage(gameCode, message, sender);

        assertEquals(chat, updatedChat);
        assertEquals(1, updatedChat.getChatMessages().size());
        assertEquals(message, updatedChat.getChatMessages().get(0).getMessage());
        assertEquals(sender, updatedChat.getChatMessages().get(0).getSender());
        verify(chatRepository).getChat(gameCode);
    }

    @Test
    public void testEndChat() {
        String gameCode = "ABC123";
        Chat chat = new Chat(gameCode);

        when(chatRepository.getChat(gameCode)).thenReturn(chat);

        Chat endedChat = chatService.endChat(gameCode);

        assertEquals(chat, endedChat);
        verify(chatRepository).removeChat(chat);
    }
}
