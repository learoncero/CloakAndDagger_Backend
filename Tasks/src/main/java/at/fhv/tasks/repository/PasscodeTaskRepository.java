package at.fhv.tasks.repository;

import at.fhv.tasks.model.PasscodeMiniGame;
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
    private final Map<String, List<PasscodeMiniGame>> taskMap;

    public PasscodeTaskRepository() {
        this.taskMap = new HashMap<>();
    }

    public PasscodeMiniGame getFirstTaskForGame(String gameCode) {
        List<PasscodeMiniGame> tasks = taskMap.get(gameCode);
        if (tasks != null && !tasks.isEmpty()) {
            for (PasscodeMiniGame task : tasks) {
                if (!task.isTaskDone()) {
                    return task;
                }
            }
        }
        return null;
    }

    public List<PasscodeMiniGame> getTasksForGame(String gameCode) {
        return taskMap.getOrDefault(gameCode, new ArrayList<>());
    }

    public void createNewInstance(String gameCode, PasscodeMiniGame miniGame) {
        List<PasscodeMiniGame> tasks = taskMap.getOrDefault(gameCode, new ArrayList<>());
        tasks.add(miniGame);
        taskMap.put(gameCode, tasks);
    }
}

