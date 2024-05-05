package at.fhv.minigames.repository;

import at.fhv.minigames.model.PasscodeMiniGame;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class PasscodeMiniGameRepository {
    private final Map<String, Map<Integer, PasscodeMiniGame>> taskMap;

    public PasscodeMiniGameRepository() {
        this.taskMap = new HashMap<>();
    }

    public void saveInstance(String gameCode, int taskId, PasscodeMiniGame miniGame) {
        Map<Integer, PasscodeMiniGame> gameTasks = taskMap.computeIfAbsent(gameCode, k -> new HashMap<>());

        gameTasks.put(taskId, miniGame);
    }

    public PasscodeMiniGame getInstance(String gameCode, int taskId) {
        Map<Integer, PasscodeMiniGame> gameTasks = taskMap.get(gameCode);

        return gameTasks != null ? gameTasks.get(taskId) : null;
    }

    public void removeInstance(String gameCode, int taskId) {
        Map<Integer, PasscodeMiniGame> gameTasks = taskMap.get(gameCode);

        if (gameTasks != null) {
            gameTasks.remove(taskId);
        }
    }
}

