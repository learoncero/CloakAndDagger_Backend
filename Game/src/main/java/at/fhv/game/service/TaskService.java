package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.Position;
import at.fhv.game.model.Task;
import at.fhv.game.model.MiniGame;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    public TaskService() {
    }

    public void addMiniGamesToGame(Game game, List<MiniGame> miniGameList, List<Position> positions) {
        List<Task> gameTasks = setTaskPositions(miniGameList, positions);
        game.setTasks(gameTasks);
    }

    private List<Task> setTaskPositions(List<MiniGame> miniGames, List<Position> positions) {
        List<Task> gameTasks = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            //get values from tasks to create new Tasks
            int miniGamesIndex = i % miniGames.size();
            int miniGameId = miniGames.get(miniGamesIndex).getId();
            String name = miniGames.get(miniGamesIndex).getName();
            String description = miniGames.get(miniGamesIndex).getDescription();

            Task newTask = new Task(miniGameId, name, description);
            newTask.setPosition(positions.get(i));
            gameTasks.add(newTask);
        }
        System.out.println("GameTasks in setTaskPositions: "+gameTasks.size());
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
}
