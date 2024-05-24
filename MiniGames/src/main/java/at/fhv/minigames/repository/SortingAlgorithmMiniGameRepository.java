package at.fhv.minigames.repository;

import at.fhv.minigames.model.SortingAlgorithmMiniGame;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class SortingAlgorithmMiniGameRepository {
    private final Map<String, Map<Integer, SortingAlgorithmMiniGame>> taskMap;

    public SortingAlgorithmMiniGameRepository() {
        this.taskMap = new HashMap<>();
    }

    public void saveInstance(String gameCode, int taskId, SortingAlgorithmMiniGame miniGame) {
        Map<Integer, SortingAlgorithmMiniGame> gameTasks = taskMap.computeIfAbsent(gameCode, k -> new HashMap<>());

        gameTasks.put(taskId, miniGame);
    }

    public SortingAlgorithmMiniGame getInstance(String gameCode, int taskId) {
        Map<Integer, SortingAlgorithmMiniGame> gameTasks = taskMap.get(gameCode);

        return gameTasks != null ? gameTasks.get(taskId) : null;
    }

    public void removeInstance(String gameCode, int taskId) {
        Map<Integer, SortingAlgorithmMiniGame> gameTasks = taskMap.get(gameCode);

        if (gameTasks != null) {
            gameTasks.remove(taskId);
        }

        if (gameTasks != null && gameTasks.isEmpty()) {
            taskMap.remove(gameCode);
        }
    }
}

