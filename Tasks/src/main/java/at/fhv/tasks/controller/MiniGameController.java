package at.fhv.tasks.controller;

import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.service.MiniGameService;
import at.fhv.tasks.service.PasscodeTaskService;
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

    @Autowired
    public MiniGameController(MiniGameService miniGameService, PasscodeTaskService passcodeTaskService) {
        this.taskService = miniGameService;
        this.passcodeTaskService = passcodeTaskService;
    }

    @GetMapping("/minigames")
    public ResponseEntity<List<MiniGame>> getAllMiniGames() {
        return ResponseEntity.ok(taskService.getAllMiniGames());
    }

    @PostMapping("/minigame/{gameCode}/start")
    public ResponseEntity<Void> startTask(@PathVariable String gameCode, @RequestBody int miniGameId) {
        MiniGame miniGame = taskService.getMiniGameById(miniGameId);
        if (miniGame != null) {
            if (miniGame instanceof PasscodeMiniGame) {
                passcodeTaskService.createNewInstance(gameCode, (PasscodeMiniGame) miniGame);
            }
        }

        return ResponseEntity.ok().build();
    }
}
