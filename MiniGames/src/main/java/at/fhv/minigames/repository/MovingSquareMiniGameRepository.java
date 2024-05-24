package at.fhv.minigames.repository;

import at.fhv.minigames.model.MovingSquareMiniGame;
import at.fhv.minigames.model.PasscodeMiniGame;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class MovingSquareMiniGameRepository {
    private final Map<String, Map<Integer, MovingSquareMiniGame>> taskMap;

    public MovingSquareMiniGameRepository() {
        this.taskMap = new HashMap<>();
    }

    public void saveInstance(String gameCode, int taskId, MovingSquareMiniGame miniGame) {
        Map<Integer, MovingSquareMiniGame> gameTasks = taskMap.computeIfAbsent(gameCode, k -> new HashMap<>());

        gameTasks.put(taskId, miniGame);
    }

    public MovingSquareMiniGame getInstance(String gameCode, int taskId) {
        Map<Integer, MovingSquareMiniGame> gameTasks = taskMap.get(gameCode);

        return gameTasks != null ? gameTasks.get(taskId) : null;
    }

    public void removeInstance(String gameCode, int taskId) {
        Map<Integer, MovingSquareMiniGame> gameTasks = taskMap.get(gameCode);

        if (gameTasks != null) {
            gameTasks.remove(taskId);
        }

        if (gameTasks != null && gameTasks.isEmpty()) {
            taskMap.remove(gameCode);
        }
    }
}
