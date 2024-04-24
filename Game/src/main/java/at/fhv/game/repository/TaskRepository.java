package at.fhv.game.repository;

import at.fhv.game.model.Task;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class TaskRepository {
    private final List<Task> tasks;

    public TaskRepository() {
        this.tasks = new ArrayList<>();
        initializeTasks();
    }

    private void initializeTasks() {
        assert tasks != null;
        tasks.add(new Task(1, "Enter the passcode", "Enter the passcode to unlock the door"));
        tasks.add(new Task(1, "Enter the passcode", "Enter the passcode to unlock the door"));
        tasks.add(new Task(1, "Enter the passcode", "Enter the passcode to unlock the door"));
        tasks.add(new Task(1, "Enter the passcode", "Enter the passcode to unlock the door"));
        tasks.add(new Task(1, "Enter the passcode", "Enter the passcode to unlock the door"));

        // TODO: Add different tasks
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
