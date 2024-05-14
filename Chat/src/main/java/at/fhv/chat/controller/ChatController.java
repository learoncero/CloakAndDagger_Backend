package at.fhv.chat.controller;

import at.fhv.chat.model.Chat;
import at.fhv.chat.model.Vote;
import at.fhv.chat.model.SendChatMessage;
import at.fhv.chat.model.AddVoteMessage;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

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
            String newResult = String.valueOf(voteResult);
            System.out.println("playerEliminated in BAckend: " + voteResult);
            Vote vote = voteService.endVote(gameCode);
            Chat chat = chatService.endChat(gameCode);
            WebClient client = WebClient.create();
            // Construct the URI using UriComponentsBuilder
            //Causes .WebClientResponseException$NotFound: 404 Not Found error, no solution found
            //everything works as expected, just the error is thrown
            String uriString = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:5010/api/game/vote/{gameCode}/voteResults/{newResult}")
                    .buildAndExpand(gameCode, newResult)
                    .toUriString();

            client.post()
                    .uri(uriString)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // Blocking call to wait for the response

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

    @Operation(summary = "Add a new vote")
    @MessageMapping("/chat/vote")
    @SendTo("/topic/vote")
    public ResponseEntity<Vote> handleNewVotes(@Payload AddVoteMessage addVoteMessage) {
        String gameCode = addVoteMessage.getGameCode();
        int vote = addVoteMessage.getPlayerId();
        Vote currentVotes = voteService.addVote(gameCode, vote);

        if (currentVotes != null) {
            return ResponseEntity.ok(currentVotes);
        }
        return ResponseEntity.notFound().build();
    }
}
