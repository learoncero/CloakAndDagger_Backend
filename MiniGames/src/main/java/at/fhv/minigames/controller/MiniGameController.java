package at.fhv.minigames.controller;

import at.fhv.minigames.model.*;
import at.fhv.minigames.model.messages.MiniGameMessage;
import at.fhv.minigames.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class MiniGameController {
    private final MiniGameService miniGameService;
    private final PasscodeMiniGameService passcodeMiniGameService;
    private final ColorSequenceMiniGameService colorSeqMiniGameService;
    private final DecipherSymbolsMiniGameService decipherSymbolsMiniGameService;
    private final SortingAlgorithmMiniGameService sortingAlgorithmMiniGameService;
    private final MovingSquareMiniGameService movingSquareMiniGameService;

    @Autowired
    public MiniGameController(MiniGameService miniGameService, PasscodeMiniGameService passcodeMiniGameService, ColorSequenceMiniGameService colorSeqMiniGameService, DecipherSymbolsMiniGameService decipherSymbolsMiniGameService, SortingAlgorithmMiniGameService sortingAlgorithmMiniGameService, MovingSquareMiniGameService movingSquareMiniGameService) {
        this.miniGameService = miniGameService;
        this.passcodeMiniGameService = passcodeMiniGameService;
        this.colorSeqMiniGameService = colorSeqMiniGameService;
        this.decipherSymbolsMiniGameService = decipherSymbolsMiniGameService;
        this.sortingAlgorithmMiniGameService = sortingAlgorithmMiniGameService;
        this.movingSquareMiniGameService = movingSquareMiniGameService;
    }

    @Operation(summary = "Get all mini games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mini games retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No mini games found")
    })
    @GetMapping("/minigames")
    public ResponseEntity<List<MiniGame>> getAllMiniGames() {
        return ResponseEntity.ok(miniGameService.getAllMiniGames());
    }

    @Operation(summary = "Start a mini game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mini game started successfully"),
            @ApiResponse(responseCode = "404", description = "Mini game not found")
    })
    @PostMapping("/minigame/{gameCode}/start")
    public ResponseEntity<Void> startTask(@PathVariable("gameCode") String gameCode, @RequestBody MiniGameMessage miniGameMessage) {
        MiniGame miniGameTemplate = miniGameService.getMiniGameById(miniGameMessage.getMiniGameId());
        MiniGame miniGameClone = miniGameService.cloneMiniGame(miniGameTemplate);
        if (miniGameClone != null) {
            if (miniGameClone instanceof PasscodeMiniGame passcodeMiniGame) {
                int randomSum = passcodeMiniGameService.generateRandomSum();
                passcodeMiniGame.setRandomSum(randomSum);
                passcodeMiniGameService.saveNewInstance(gameCode, miniGameMessage.getTaskId(), passcodeMiniGame);
            }

            if (miniGameClone instanceof ColorSeqMiniGame colorSeqMiniGame) {
                colorSeqMiniGameService.saveNewInstance(gameCode, miniGameMessage.getTaskId(), colorSeqMiniGame);
            }

            if (miniGameClone instanceof DecipherSymbolsMiniGame decipherSymbolsMiniGame) {
                decipherSymbolsMiniGameService.saveNewInstance(gameCode, miniGameMessage.getTaskId(), decipherSymbolsMiniGame);
            }

            if (miniGameClone instanceof SortingAlgorithmMiniGame sortingAlgorithmMiniGame) {
                sortingAlgorithmMiniGameService.saveNewInstance(gameCode, miniGameMessage.getTaskId(), sortingAlgorithmMiniGame);
            }

            if (miniGameClone instanceof MovingSquareMiniGame movingSquareMiniGame) {
                movingSquareMiniGameService.saveInstance(gameCode, miniGameMessage.getTaskId(), movingSquareMiniGame);
            }
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel a mini game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mini game cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Mini game not found")
    })
    @PostMapping("/minigame/{gameCode}/cancel")
    public ResponseEntity<Void> cancelTask(@PathVariable("gameCode") String gameCode, @RequestBody MiniGameMessage miniGameMessage) {
        MiniGame miniGame = miniGameService.getMiniGameById(miniGameMessage.getMiniGameId());
        if (miniGame != null) {
            if (miniGame instanceof PasscodeMiniGame) {
                passcodeMiniGameService.deleteInstance(gameCode, miniGameMessage.getTaskId());
            }

            if (miniGame instanceof ColorSeqMiniGame) {
                colorSeqMiniGameService.deleteInstance(gameCode, miniGameMessage.getTaskId());
            }

            if (miniGame instanceof DecipherSymbolsMiniGame) {
                decipherSymbolsMiniGameService.deleteInstance(gameCode, miniGameMessage.getTaskId());
            }

            if (miniGame instanceof SortingAlgorithmMiniGame) {
                sortingAlgorithmMiniGameService.deleteInstance(gameCode, miniGameMessage.getTaskId());
            }

            if (miniGame instanceof MovingSquareMiniGame) {
                movingSquareMiniGameService.deleteInstance(gameCode, miniGameMessage.getTaskId());
            }
        }

        return ResponseEntity.ok().build();
    }
}
