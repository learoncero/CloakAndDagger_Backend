package at.fhv.chat.controller;

import at.fhv.chat.model.Chat;
import at.fhv.chat.model.messages.ChatStartMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ChatController {

    public ChatController() {
    }

    @PostMapping("/chat")
    public ResponseEntity<Chat> startChat(@RequestBody ChatStartMessage chatStartMessage) {
        Chat chat = new Chat(chatStartMessage.getGameCode(), chatStartMessage.getActivePlayersUsernames());
        return ResponseEntity.ok(chat);
    }
}
