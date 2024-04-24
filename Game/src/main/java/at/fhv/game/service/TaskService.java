package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.Task;
import at.fhv.game.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTasksToGame(Game game) {
        game.setTasks(getAllTasks());
    }

    private List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }
}
