package at.fhv.tasks.controller;

import at.fhv.tasks.model.ColorSeqMiniGame;
import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.model.messages.StartMiniGameMessage;
import at.fhv.tasks.service.ColorSequenceMiniGameService;
import at.fhv.tasks.service.MiniGameService;
import at.fhv.tasks.service.PasscodeTaskService;
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
    private final MiniGameService taskService;
    private final PasscodeTaskService passcodeTaskService;
    private final ColorSequenceMiniGameService colorSeqMiniGameService;

    @Autowired
    public MiniGameController(MiniGameService miniGameService, PasscodeTaskService passcodeTaskService, ColorSequenceMiniGameService colorSeqMiniGameService) {
        this.taskService = miniGameService;
        this.passcodeTaskService = passcodeTaskService;
        this.colorSeqMiniGameService = colorSeqMiniGameService;

    }

    @Operation(summary = "Get all mini games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mini games retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No mini games found")
    })
    @GetMapping("/minigames")
    public ResponseEntity<List<MiniGame>> getAllMiniGames() {
        return ResponseEntity.ok(taskService.getAllMiniGames());
    }

    @Operation(summary = "Start a mini game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mini game started successfully"),
            @ApiResponse(responseCode = "404", description = "Mini game not found")
    })
    @PostMapping("/minigame/{gameCode}/start")
    public ResponseEntity<Void> startTask(@PathVariable("gameCode") String gameCode, @RequestBody StartMiniGameMessage startMiniGameMessage) {
        MiniGame miniGameTemplate = taskService.getMiniGameById(startMiniGameMessage.getMiniGameId());
        MiniGame miniGameClone = taskService.cloneMiniGame(miniGameTemplate);
        if (miniGameClone != null) {
            if (miniGameClone instanceof PasscodeMiniGame passcodeMiniGame) {
                int randomSum = passcodeTaskService.generateRandomSum();
                passcodeMiniGame.setRandomSum(randomSum);
                passcodeTaskService.saveNewInstance(gameCode, startMiniGameMessage.getTaskId(), passcodeMiniGame);
            }
            if (miniGameClone instanceof ColorSeqMiniGame) {
                ColorSeqMiniGame colorSeqMiniGame = (ColorSeqMiniGame) miniGameClone;
                colorSeqMiniGameService.saveNewInstance(gameCode, startMiniGameMessage.getTaskId(), colorSeqMiniGame);
            }
        }

        return ResponseEntity.ok().build();
    }
}
