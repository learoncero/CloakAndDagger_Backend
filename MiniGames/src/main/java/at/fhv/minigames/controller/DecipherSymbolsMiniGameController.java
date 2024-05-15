package at.fhv.minigames.controller;

import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import at.fhv.minigames.model.messages.DecipherSymbolsMiniGameMessage;
import at.fhv.minigames.service.DecipherSymbolsMiniGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
public class DecipherSymbolsMiniGameController {
    private final DecipherSymbolsMiniGameService decipherSymbolsMiniGameService;

    @Autowired
    public DecipherSymbolsMiniGameController(DecipherSymbolsMiniGameService decipherSymbolsMiniGameService) {
        this.decipherSymbolsMiniGameService = decipherSymbolsMiniGameService;
    }

    @Operation(summary = "Get shuffled symbols for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shuffled symbols retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/decipher/{gameCode}/getShuffledSymbols")
    public ResponseEntity<List<String>> getShuffledSymbols(@PathVariable("gameCode") String gameCode, @RequestBody DecipherSymbolsMiniGameMessage submission) {
        DecipherSymbolsMiniGame decipherSymbolsMiniGame = decipherSymbolsMiniGameService.getInstance(gameCode, submission.getTaskId());

        if (decipherSymbolsMiniGame != null) {
            List<String> sequence = decipherSymbolsMiniGameService.createShuffledSymbols(decipherSymbolsMiniGame.getSymbols(), submission.getCurrentRound());
            decipherSymbolsMiniGame.setShuffledSymbols(sequence);

            return ResponseEntity.ok(sequence);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get correct symbol for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct symbol retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/decipher/{gameCode}/getCorrectSymbol")
    public ResponseEntity<List<String>> getCorrectSymbol(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        DecipherSymbolsMiniGame decipherSymbolsMiniGame = decipherSymbolsMiniGameService.getInstance(gameCode, taskId);

        if (decipherSymbolsMiniGame != null) {
            String correctSymbol = decipherSymbolsMiniGameService.getCorrectSymbol(decipherSymbolsMiniGame.getShuffledSymbols());
            decipherSymbolsMiniGame.setCorrectSymbol(correctSymbol);

            return ResponseEntity.ok(List.of(correctSymbol));
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Submit symbol for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Symbol submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect symbol")
    })
    @PostMapping("/decipher/{gameCode}/submit")
    public ResponseEntity<Boolean> submitSymbol(@RequestBody DecipherSymbolsMiniGameMessage submission, @PathVariable("gameCode") String gameCode) {

        if (decipherSymbolsMiniGameService.verifySymbol(submission.getSymbol(), submission.getTaskId(), gameCode, submission.getCurrentRound())) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
