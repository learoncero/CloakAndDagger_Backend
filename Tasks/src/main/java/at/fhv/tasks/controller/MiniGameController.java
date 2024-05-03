package at.fhv.tasks.controller;

import at.fhv.tasks.model.ColorSeqMiniGame;
import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.model.messages.StartMiniGameMessage;
import at.fhv.tasks.service.ColorSequenceMiniGameService;
import at.fhv.tasks.service.MiniGameService;
import at.fhv.tasks.service.PasscodeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/minigames")
    public ResponseEntity<List<MiniGame>> getAllMiniGames() {
        return ResponseEntity.ok(taskService.getAllMiniGames());
    }

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
