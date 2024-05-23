package at.fhv.minigames.controller;

import at.fhv.minigames.model.SortingAlgorithmMiniGame;
import at.fhv.minigames.model.messages.SortingAlgorithmMiniGameMessage;
import at.fhv.minigames.service.SortingAlgorithmMiniGameService;
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
public class SortingAlgorithmMiniGameController {
    private final SortingAlgorithmMiniGameService sortingAlgorithmMiniGameService;

    @Autowired
    public SortingAlgorithmMiniGameController(SortingAlgorithmMiniGameService sortingAlgorithmMiniGameService) {
        this.sortingAlgorithmMiniGameService = sortingAlgorithmMiniGameService;
    }

    @Operation(summary = "Get shuffled boxes for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shuffled boxes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game or task not found")
    })
    @PostMapping("/sorting/{gameCode}/getShuffledBoxes")
    public ResponseEntity<List<Integer>> getShuffledBoxes(@PathVariable("gameCode") String gameCode, @RequestBody SortingAlgorithmMiniGameMessage submission) {
        SortingAlgorithmMiniGame sortingAlgorithmMiniGame = sortingAlgorithmMiniGameService.getInstance(gameCode, submission.getTaskId());

        if (sortingAlgorithmMiniGame != null) {
            List<Integer> shuffledBoxes = sortingAlgorithmMiniGameService.createShuffledBoxes(sortingAlgorithmMiniGame.getBoxes());
            sortingAlgorithmMiniGame.setShuffledBoxes(shuffledBoxes);

            return ResponseEntity.ok(shuffledBoxes);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Submit sorted boxes for a game task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Boxes submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect sorting")
    })
    @PostMapping("/sorting/{gameCode}/submit")
    public ResponseEntity<Boolean> submitSorting(@RequestBody SortingAlgorithmMiniGameMessage submission, @PathVariable("gameCode") String gameCode) {
        boolean isSorted = sortingAlgorithmMiniGameService.verifySorting(submission.getBoxes(), submission.getTaskId(), gameCode);

        if (isSorted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}

