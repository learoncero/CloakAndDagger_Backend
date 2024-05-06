package at.fhv.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    private static int nextId = 1;
    private int id;
    private String message;
    private String sender;

    public ChatMessage(String message, String sender) {
        this.id = nextId++;
        this.message = message;
        this.sender = sender;
    }
}
