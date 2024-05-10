package at.fhv.minigames.controller;

import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import at.fhv.minigames.model.MiniGame;
import at.fhv.minigames.model.PasscodeMiniGame;
import at.fhv.minigames.model.messages.MiniGameMessage;
import at.fhv.minigames.service.ColorSequenceMiniGameService;
import at.fhv.minigames.service.DecipherSymbolsMiniGameService;
import at.fhv.minigames.service.MiniGameService;
import at.fhv.minigames.service.PasscodeMiniGameService;
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

    @Autowired
    public MiniGameController(MiniGameService miniGameService, PasscodeMiniGameService passcodeMiniGameService, ColorSequenceMiniGameService colorSeqMiniGameService, DecipherSymbolsMiniGameService decipherSymbolsMiniGameService) {
        this.miniGameService = miniGameService;
        this.passcodeMiniGameService = passcodeMiniGameService;
        this.colorSeqMiniGameService = colorSeqMiniGameService;
        this.decipherSymbolsMiniGameService = decipherSymbolsMiniGameService;
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
        }

        return ResponseEntity.ok().build();
    }
}
