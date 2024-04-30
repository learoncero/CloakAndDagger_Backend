package at.fhv.tasks.controller;

import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.model.messages.PasscodeTaskMessage;
import at.fhv.tasks.service.PasscodeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class PasscodeMiniGameController {
    private final PasscodeTaskService passcodeTaskService;

    @Autowired
    public PasscodeMiniGameController(PasscodeTaskService passcodeTaskService) {
        this.passcodeTaskService = passcodeTaskService;
    }

    @PostMapping("/passcode/{gameCode}/add")
    public ResponseEntity<Integer> addToSum(@RequestBody PasscodeTaskMessage passcodeTaskMessage, @PathVariable("gameCode") String gameCode) {
        int currentValue = passcodeTaskMessage.getValue();
        int taskId = passcodeTaskMessage.getTaskId();
        int currentSum = passcodeTaskService.addToSum(currentValue, taskId, gameCode);

        if (currentSum == -1) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(currentSum);
        }
    }

    @PostMapping("/passcode/{gameCode}/random")
    public ResponseEntity<Integer> getRandomSum(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        PasscodeMiniGame passcodeMiniGame = passcodeTaskService.getInstance(gameCode, taskId);
        return ResponseEntity.ok(passcodeMiniGame.getRandomSum());
    }

    @PostMapping("/passcode/{gameCode}/reset")
    public ResponseEntity<Integer> resetSum(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        PasscodeMiniGame passcodeMiniGame = passcodeTaskService.getInstance(gameCode, taskId);
        passcodeMiniGame.setCurrentSum(0);
        return ResponseEntity.ok(0);
    }
}
