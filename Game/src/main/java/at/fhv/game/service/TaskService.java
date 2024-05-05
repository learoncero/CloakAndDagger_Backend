package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.MiniGame;
import at.fhv.game.model.Position;
import at.fhv.game.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < positions.size(); i++) {
            int miniGamesIndex = i % miniGames.size();
            int miniGameId = miniGames.get(miniGamesIndex).getId();
            String name = miniGames.get(miniGamesIndex).getTitle();
            String description = miniGames.get(miniGamesIndex).getDescription();

            Task newTask = new Task(miniGameId, name, description);
            newTask.setPosition(positions.get(i));
            gameTasks.add(newTask);
        }

        return gameTasks;
    }

    public boolean taskDone(Game game, int taskId) {
        for (Task t : game.getTasks()) {
            if (t.getTaskId() == taskId) {
                t.setCompleted(true);
                return true;
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
