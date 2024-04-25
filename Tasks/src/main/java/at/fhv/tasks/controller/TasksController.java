package at.fhv.tasks.controller;

import at.fhv.tasks.model.Task;
import at.fhv.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/*
@Controller
@RequestMapping("/api")
public class TasksController {
    private final TaskService taskService;

    @Autowired
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks/create")
    public ResponseEntity<?> createTasks(@RequestBody TaskCreateMessage createMessage) {
        try {
            List<Task> tasks = taskService.createTasksForGame(createMessage.getGameCode());

            return ResponseEntity.ok().body(tasks);
        } catch (Exception e) {
            System.out.println("Error creating tasks: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(500, "Error creating tasks: " + e.getMessage(), null));
        }
    }


}

 */
