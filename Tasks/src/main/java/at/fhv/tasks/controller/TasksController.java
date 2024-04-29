package at.fhv.tasks.controller;

import at.fhv.tasks.service.PasscodeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class TasksController {
    private final PasscodeTaskService passcodeTaskService;

    @Autowired
    public TasksController(PasscodeTaskService passcodeTaskService) {
        this.passcodeTaskService = passcodeTaskService;
    }

    @PostMapping("/task/create")
    public ResponseEntity<Void> createTasksForGame(@RequestParam String gameCode) {
        passcodeTaskService.createTasksForGame(gameCode);
        return ResponseEntity.ok().build();
    }
}
