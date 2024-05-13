package at.fhv.minigames.controller;

import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.model.messages.ColorSeqMiniGameMessage;
import at.fhv.minigames.service.ColorSequenceMiniGameService;
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
public class ColorSequenceMiniGameController {


    private final ColorSequenceMiniGameService colorSequenceMiniGameService;

    @Autowired
    public ColorSequenceMiniGameController(ColorSequenceMiniGameService colorSequenceMiniGameService) {
        this.colorSequenceMiniGameService = colorSequenceMiniGameService;
    }

    @Operation(summary = "Get shuffled colors for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shuffled colors retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/colors/{gameCode}/getShuffledColors")
    public ResponseEntity<List<String>> getShuffledColors(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        ColorSeqMiniGame colorSeqMiniGame = colorSequenceMiniGameService.getInstance(gameCode, taskId);

        if (colorSeqMiniGame != null) {
            List<String> sequence = colorSequenceMiniGameService.createShuffledSequence(colorSeqMiniGame.getColors());

            return ResponseEntity.ok(sequence);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Submit colors for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colors submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect sequence")
    })
    @PostMapping("/colors/{gameCode}/submit")
    public ResponseEntity<String> submitColors(@RequestBody ColorSeqMiniGameMessage submission, @PathVariable("gameCode") String gameCode) {
        if (colorSequenceMiniGameService.verifySequence(submission.getColors(), submission.getShuffledColors(), submission.getTaskId(), gameCode)) {
            return ResponseEntity.ok("Task Completed");
        } else {
            return ResponseEntity.badRequest().body("Incorrect sequence");
        }
    }

    @Operation(summary = "Get initial colors for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Initial colors retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/colors/{gameCode}/getInitialColors")
    public ResponseEntity<List<String>> getInitialColor(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        ColorSeqMiniGame colorSeqMiniGame = colorSequenceMiniGameService.getInstance(gameCode, taskId);

        if (colorSeqMiniGame != null) {
            return ResponseEntity.ok(colorSeqMiniGame.getColors());
        }

        return ResponseEntity.notFound().build();
    }

}
