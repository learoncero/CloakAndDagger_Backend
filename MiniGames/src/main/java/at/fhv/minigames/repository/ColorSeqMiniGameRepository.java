package at.fhv.minigames.repository;

import at.fhv.minigames.model.ColorSeqMiniGame;
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
        Map<Integer, ColorSeqMiniGame> gameTasks = taskMap.computeIfAbsent(gameCode, k -> new HashMap<>());

        gameTasks.put(taskId, miniGame);
    }

    public ColorSeqMiniGame getInstance(String gameCode, int taskId) {
        Map<Integer, ColorSeqMiniGame> gameTasks = taskMap.get(gameCode);

        return gameTasks != null ? gameTasks.get(taskId) : null;
    }

    public void removeInstance(String gameCode, int taskId) {
        Map<Integer, ColorSeqMiniGame> gameTasks = taskMap.get(gameCode);

        if (gameTasks != null) {
            gameTasks.remove(taskId);
        }

        if (gameTasks != null && gameTasks.isEmpty()) {
            taskMap.remove(gameCode);
        }
    }
}

