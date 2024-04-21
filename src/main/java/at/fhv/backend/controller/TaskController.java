package at.fhv.backend.controller;

import at.fhv.backend.model.messages.PasscodeTaskMessage;
import at.fhv.backend.service.GameService;
import at.fhv.backend.service.PlayerService;
import at.fhv.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public TaskController(TaskService taskService, PlayerService playerService, GameService gameService) {
        this.taskService = taskService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @PostMapping("/task/passcode/add")
    public ResponseEntity<Integer> addToSum(@RequestBody PasscodeTaskMessage passcodeTaskMessage) {
        System.out.println("Adding " + passcodeTaskMessage.getValue() + " to sum");
        taskService.addToSum(passcodeTaskMessage.getValue());

        System.out.println("Current sum: " + taskService.getCurrentSum());
        return ResponseEntity.ok(taskService.getCurrentSum());
    }

    @GetMapping("/task/passcode/random")
    public ResponseEntity<Integer> getRandomSum() {
        int randomValue = taskService.generateRandomSum();
        System.out.println("Random value: " + randomValue);
        return ResponseEntity.ok(randomValue);
    }

    @PostMapping("/task/passcode/reset")
    public ResponseEntity<Integer> resetSum() {
        taskService.resetSum();
        return ResponseEntity.ok(taskService.getCurrentSum());
    }
}
