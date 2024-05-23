package at.fhv.chat.controller;

import at.fhv.chat.model.*;
import at.fhv.chat.service.ChatService;
import at.fhv.chat.service.VoteService;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/api")
public class ChatController {
    ChatService chatService;
    VoteService voteService;
    private final org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();

    @Autowired
    public ChatController(ChatService chatService, VoteService voteService) {
        this.chatService = chatService;
        this.voteService = voteService;
    }

    @Operation(summary = "Start a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat started successfully"),
            @ApiResponse(responseCode = "404", description = "Chat could not be started successfully")
    })
    @PostMapping("/chat/{gameCode}/start")
    public ResponseEntity<Chat> startChat(@PathVariable String gameCode) {
        Chat chat = chatService.startChat(gameCode);
        voteService.startVote(gameCode);

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
    public void endChat(@PathVariable String gameCode) {
        Integer voteResult = voteService.getVoteResult(gameCode);
        if (voteResult != -2){
            Vote finalVotes = voteService.getVoteByCode(gameCode);
            finalVotes.setVoteResult(voteResult);
            System.out.println("Final votes: " + finalVotes);
            Vote vote = voteService.endVote(gameCode);
            Chat chat = chatService.endChat(gameCode);
            WebClient client = WebClient.create();

            client.post()
                    .uri("http://localhost:5010/api/game/vote/voteResults")
                    .body(BodyInserters.fromValue(finalVotes))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }
    }

    @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/messages")
    public ResponseEntity<Chat> handleSendMessage(@Payload SendChatMessage sendChatMessage) {
        String gameCode = sendChatMessage.getGameCode();
        String message = sendChatMessage.getMessage();
        String sender = sendChatMessage.getSender();

        Chat chat = chatService.sendMessage(gameCode, message, sender);

        if (chat != null) {
            return ResponseEntity.ok(chat);
        }

        return ResponseEntity.notFound().build();
    }

    @MessageMapping("/chat/vote")
    @SendTo("/topic/vote")
    public ResponseEntity<Vote> handleNewVotes(@Payload AddVoteMessage addVoteMessage) {
        String gameCode = addVoteMessage.getGameCode();
        VoteEvent vote = addVoteMessage.getVoteEvent();
        Vote currentVotes = voteService.addVote(gameCode, vote);

        if (currentVotes != null) {
            return ResponseEntity.ok(currentVotes);
        }
        return ResponseEntity.notFound().build();
    }
}
