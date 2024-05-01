package at.fhv.tasks.repository;

import at.fhv.tasks.model.ColorSeqMiniGame;
import at.fhv.tasks.model.PasscodeMiniGame;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class ColorSeqMiniGameRepository {
    private final Map<String, Map<Integer, ColorSeqMiniGame>> taskMap;

    public ColorSeqMiniGameRepository() {
        this.taskMap = new HashMap<>();
    }

    public void saveInstance(String gameCode, int taskId, ColorSeqMiniGame miniGame) {
        // Retrieve the map for the specified game code or create a new one if it doesn't exist
        Map<Integer, ColorSeqMiniGame> gameTasks = taskMap.computeIfAbsent(gameCode, k -> new HashMap<>());

        gameTasks.put(taskId, miniGame);
    }

    public ColorSeqMiniGame getInstance(String gameCode, int taskId) {
        // Retrieve the map for the specified game code
        Map<Integer, ColorSeqMiniGame> gameTasks = taskMap.get(gameCode);

        // Return the mini-game instance for the specified task ID if it exists
        return gameTasks != null ? gameTasks.get(taskId) : null;
    }

    // Method to remove a mini-game instance by game code and task ID
    public void removeInstance(String gameCode, int taskId) {
        // Retrieve the map for the specified game code
        Map<Integer, ColorSeqMiniGame> gameTasks = taskMap.get(gameCode);

        // Remove the mini-game instance for the specified task ID if it exists
        if (gameTasks != null) {
            gameTasks.remove(taskId);
        }
    }
}

