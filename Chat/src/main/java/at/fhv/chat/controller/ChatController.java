package at.fhv.chat.controller;

import at.fhv.chat.model.Chat;
import at.fhv.chat.model.messages.ChatStartMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ChatController {

    public ChatController() {
    }

    @Operation(summary = "Start a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat started successfully")
    })
    @PostMapping("/chat")
    public ResponseEntity<Chat> startChat(@RequestBody ChatStartMessage chatStartMessage) {
        Chat chat = new Chat(chatStartMessage.getGameCode(), chatStartMessage.getActivePlayersUsernames());
        return ResponseEntity.ok(chat);
    }
}
