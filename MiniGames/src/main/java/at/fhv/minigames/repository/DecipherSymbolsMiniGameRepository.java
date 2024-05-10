package at.fhv.minigames.repository;

import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class DecipherSymbolsMiniGameRepository {
    private final Map<String, Map<Integer, DecipherSymbolsMiniGame>> taskMap;

    public DecipherSymbolsMiniGameRepository() {
        this.taskMap = new HashMap<>();
    }

    public void saveInstance(String gameCode, int taskId, DecipherSymbolsMiniGame miniGame) {
        Map<Integer, DecipherSymbolsMiniGame> gameTasks = taskMap.computeIfAbsent(gameCode, k -> new HashMap<>());

        gameTasks.put(taskId, miniGame);
    }

    public DecipherSymbolsMiniGame getInstance(String gameCode, int taskId) {
        Map<Integer, DecipherSymbolsMiniGame> gameTasks = taskMap.get(gameCode);

        return gameTasks != null ? gameTasks.get(taskId) : null;
    }

    public void removeInstance(String gameCode, int taskId) {
        Map<Integer, DecipherSymbolsMiniGame> gameTasks = taskMap.get(gameCode);

        if (gameTasks != null) {
            gameTasks.remove(taskId);
        }

        if (gameTasks != null && gameTasks.isEmpty()) {
            taskMap.remove(gameCode);
        }
    }
}
