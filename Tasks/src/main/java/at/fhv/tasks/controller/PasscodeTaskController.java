package at.fhv.tasks.controller;

import at.fhv.tasks.model.messages.PasscodeTaskMessage;
import at.fhv.tasks.service.PasscodeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class PasscodeTaskController {
    private final PasscodeTaskService passcodeTaskService;

    @Autowired
    public PasscodeTaskController(PasscodeTaskService passcodeTaskService) {
        this.passcodeTaskService = passcodeTaskService;
    }

    @PostMapping("/task/passcode/create")
    public ResponseEntity<Void> createTasksForGame(@RequestParam String gameCode) {
        passcodeTaskService.createTasksForGame(gameCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task/{gameCode}/passcode/add")
    public ResponseEntity<Integer> addToSum(@RequestBody PasscodeTaskMessage passcodeTaskMessage, @PathVariable String gameCode) {
        if (passcodeTaskService.isTaskDone(gameCode)) {
            return ResponseEntity.ok(passcodeTaskService.getCurrentSum(gameCode));
        }

        int currentValue = passcodeTaskMessage.getValue();
        passcodeTaskService.addToSum(currentValue, gameCode);
        int currentSum = passcodeTaskService.getCurrentSum(gameCode);
        int randomSum = passcodeTaskService.getRandomSum();

        if (currentSum == randomSum) {
            passcodeTaskService.setTaskDone(true);
            System.out.println("Task done");
        } else if (currentSum > randomSum) {
            passcodeTaskService.resetSum(gameCode);
        }

        return ResponseEntity.ok(currentSum);
    }

    @GetMapping("/task/{gameCode}/passcode/random")
    public ResponseEntity<Integer> getRandomSum(@PathVariable String gameCode) {
        int randomValue = passcodeTaskService.generateRandomSum(gameCode);
        System.out.println("Random value: " + randomValue);
        return ResponseEntity.ok(randomValue);
    }

    @PostMapping("/task/{gameCode}/passcode/reset")
    public ResponseEntity<Integer> resetSum(@PathVariable String gameCode) {
        if (passcodeTaskService.isTaskDone(gameCode)) {
            return ResponseEntity.badRequest().build();
        }

        passcodeTaskService.resetSum(gameCode);
        return ResponseEntity.ok(passcodeTaskService.getCurrentSum(gameCode));
    }
}
