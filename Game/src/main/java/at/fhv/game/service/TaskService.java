package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.MiniGame;
import at.fhv.game.model.Position;
import at.fhv.game.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class TaskService {

    public TaskService() {
    }

    public void addMiniGamesToGame(Game game, List<MiniGame> miniGames, List<Position> positions) {
        List<Task> gameTasks = setTaskPositions(miniGames, positions);
        game.setTasks(gameTasks);
    }

    private List<Task> setTaskPositions(List<MiniGame> miniGames, List<Position> positions) {
        List<Task> gameTasks = new ArrayList<>();
        Random random = new Random();

        // Shuffle the miniGames list
        Collections.shuffle(miniGames, random);

        for (int i = 0; i < positions.size(); i++) {
            int miniGamesIndex = i % miniGames.size();
            MiniGame miniGame = miniGames.get(miniGamesIndex);
            int miniGameId = miniGame.getId();
            String name = miniGame.getTitle();
            String description = miniGame.getDescription();

            Task newTask = new Task(miniGameId, name, description);
            newTask.setPosition(positions.get(i));
            gameTasks.add(newTask);
        }

        return gameTasks;
    }

    public boolean taskDone(Game game, int taskId) {
        for (Task t : game.getTasks()) {
            if (t.getTaskId() == taskId) {
                if (t.isCompleted()) {
                    return false;
                } else {
                    t.setCompleted(true);
                    return true;
                }
            }
        }

        return false;
    }

    public Task getTaskById(Game game, int taskId) {
        for (Task t : game.getTasks()) {
            if (t.getTaskId() == taskId) {
                return t;
            }
        }

        return null;
    }

    public boolean getStatus(Game game, int taskId) {
        return getTaskById(game, taskId).isActive();
    }

    public void setStatus(Game game, int taskId, boolean status) {
        getTaskById(game, taskId).setActive(status);
    }
}
