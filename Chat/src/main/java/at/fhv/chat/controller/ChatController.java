package at.fhv.chat.controller;

import at.fhv.chat.model.Chat;
import at.fhv.chat.model.SendChatMessage;
import at.fhv.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ChatController {
    ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(summary = "Start a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat started successfully"),
            @ApiResponse(responseCode = "404", description = "Chat could not be started successfully")
    })
    @PostMapping("/chat/{gameCode}/start")
    public ResponseEntity<Chat> startChat(@PathVariable String gameCode) {
        Chat chat = chatService.startChat(gameCode);

        if (chat != null) {
            return ResponseEntity.ok(chat);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "End a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat ended successfully"),
            @ApiResponse(responseCode = "404", description = "Chat not found")
    })
    @PostMapping("/{gameCode}/chat/end")
    public ResponseEntity<Void> endChat(@PathVariable String gameCode) {
        Chat chat = chatService.endChat(gameCode);

        if (chat != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/messages")
    public ResponseEntity<Chat> handleReportBody(@Payload SendChatMessage sendChatMessage) {
        String gameCode = sendChatMessage.getGameCode();
        String message = sendChatMessage.getMessage();
        String sender = sendChatMessage.getSender();

        Chat chat = chatService.sendMessage(gameCode, message, sender);

        if (chat != null) {
            return ResponseEntity.ok(chat);
        }

        return ResponseEntity.notFound().build();
    }

}
