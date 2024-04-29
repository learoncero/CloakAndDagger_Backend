package at.fhv.tasks.repository;

import at.fhv.tasks.model.PasscodeTask;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
public class PasscodeTaskRepository {
    private final Map<String, List<PasscodeTask>> taskMap;

    public PasscodeTaskRepository() {
        this.taskMap = new HashMap<>();
    }

    public void saveTasksForGame(String gameCode, List<PasscodeTask> tasks) {
        taskMap.put(gameCode, tasks);
    }

    public PasscodeTask getFirstTaskForGame(String gameCode) {
        List<PasscodeTask> tasks = taskMap.get(gameCode);
        if (tasks != null && !tasks.isEmpty()) {
            for (PasscodeTask task : tasks) {
                if (!task.isTaskDone()) {
                    return task;
                }
            }
        }
        return null;
    }

    public List<PasscodeTask> getTasksForGame(String gameCode) {
        return taskMap.getOrDefault(gameCode, new ArrayList<>());
    }
}

