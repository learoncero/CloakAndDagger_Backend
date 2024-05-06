package at.fhv.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Chat {
    @Schema(description = "The unique identifier for the chat")
    private String id;

    @Schema(description = "List of usernames of active players in the chat")
    private List<ChatMessage> chatMessages;

    public Chat(String gameCode) {
        this.id = "Chat_" + gameCode;
        this.chatMessages = new ArrayList<>();
    }

    public void addMessage(String message, String sender) {
        chatMessages.add(new ChatMessage(message, sender));
    }
}
