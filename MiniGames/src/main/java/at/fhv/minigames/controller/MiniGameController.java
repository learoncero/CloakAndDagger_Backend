package at.fhv.minigames.controller;

import at.fhv.minigames.service.MiniGameService;
import at.fhv.minigames.service.PasscodeMiniGameService;
import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.model.MiniGame;
import at.fhv.minigames.model.PasscodeMiniGame;
import at.fhv.minigames.model.messages.StartMiniGameMessage;
import at.fhv.minigames.service.ColorSequenceMiniGameService;
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

    @Autowired
    public MiniGameController(MiniGameService miniGameService, PasscodeMiniGameService passcodeMiniGameService, ColorSequenceMiniGameService colorSeqMiniGameService) {
        this.miniGameService = miniGameService;
        this.passcodeMiniGameService = passcodeMiniGameService;
        this.colorSeqMiniGameService = colorSeqMiniGameService;

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
    public ResponseEntity<Void> startTask(@PathVariable("gameCode") String gameCode, @RequestBody StartMiniGameMessage startMiniGameMessage) {
        MiniGame miniGameTemplate = miniGameService.getMiniGameById(startMiniGameMessage.getMiniGameId());
        MiniGame miniGameClone = miniGameService.cloneMiniGame(miniGameTemplate);
        if (miniGameClone != null) {
            if (miniGameClone instanceof PasscodeMiniGame passcodeMiniGame) {
                int randomSum = passcodeMiniGameService.generateRandomSum();
                passcodeMiniGame.setRandomSum(randomSum);
                passcodeMiniGameService.saveNewInstance(gameCode, startMiniGameMessage.getTaskId(), passcodeMiniGame);
            }
            if (miniGameClone instanceof ColorSeqMiniGame colorSeqMiniGame) {
                colorSeqMiniGameService.saveNewInstance(gameCode, startMiniGameMessage.getTaskId(), colorSeqMiniGame);
            }
        }

        return ResponseEntity.ok().build();
    }
}
