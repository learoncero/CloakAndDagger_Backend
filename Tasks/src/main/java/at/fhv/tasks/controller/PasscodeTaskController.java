package at.fhv.tasks.controller;

import at.fhv.tasks.model.messages.PasscodeTaskMessage;
import at.fhv.tasks.service.PasscodeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class PasscodeTaskController {
    private final PasscodeTaskService passcodeTaskService;

    @Autowired
    public PasscodeTaskController(PasscodeTaskService passcodeTaskService) {
        this.passcodeTaskService = passcodeTaskService;
    }

    @PostMapping("/task/passcode/add")
    public ResponseEntity<Integer> addToSum(@RequestBody PasscodeTaskMessage passcodeTaskMessage) {
        if (passcodeTaskService.isTaskDone()) {
            return ResponseEntity.ok(passcodeTaskService.getCurrentSum());
        }

        int currentValue = passcodeTaskMessage.getValue();
        passcodeTaskService.addToSum(currentValue);
        int currentSum = passcodeTaskService.getCurrentSum();
        int randomSum = passcodeTaskService.getRandomSum();

        if (currentSum == randomSum) {
            passcodeTaskService.setTaskDone(true);
            System.out.println("Task done");
        } else if (currentSum > randomSum) {
            passcodeTaskService.resetSum();
        }

        return ResponseEntity.ok(currentSum);
    }

    @GetMapping("/task/passcode/random")
    public ResponseEntity<Integer> getRandomSum() {
        int randomValue = passcodeTaskService.generateRandomSum();
        System.out.println("Random value: " + randomValue);
        return ResponseEntity.ok(randomValue);
    }

    @PostMapping("/task/passcode/reset")
    public ResponseEntity<Integer> resetSum() {
        if (passcodeTaskService.isTaskDone()) {
            return ResponseEntity.badRequest().build();
        }

        passcodeTaskService.resetSum();
        return ResponseEntity.ok(passcodeTaskService.getCurrentSum());
    }
}
