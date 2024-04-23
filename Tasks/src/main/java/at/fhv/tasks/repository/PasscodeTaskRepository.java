package at.fhv.tasks.repository;

import at.fhv.tasks.model.PasscodeTask;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class PasscodeTaskRepository {
    private final List<PasscodeTask> tasks;

    public PasscodeTaskRepository(List<PasscodeTask> tasks) {
        this.tasks = tasks;
        initializeTasks();
    }

    private void initializeTasks() {
        tasks.add(new PasscodeTask());
        // TODO: Add more tasks
    }

    public PasscodeTask getAllTasks() {
        return tasks.get(0);
    }

}
