/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          24/04/2024
 */

package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.Position;
import at.fhv.game.model.Task;
import at.fhv.game.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public void addTasksToGame(Game game, List<Task> minigames, List<Position> positions) {
        List<Task> gameTasks = setTaskPositions(minigames, positions);
        game.setTasks(gameTasks);
    }

    private List<Task> setTaskPositions(List<Task> minigames, List<Position> positions) {
        List<Task> gameTasks = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            //get values from minigames to create new Tasks
            int miniGamesIndex = i % minigames.size();
            int miniGameId = minigames.get(miniGamesIndex).getMiniGameId();
            String title = minigames.get(miniGamesIndex).getTitle();
            String description = minigames.get(miniGamesIndex).getDescription();

            Task newTask = new Task(miniGameId, title, description);
            newTask.setPosition(positions.get(i));
            gameTasks.add(newTask);
        }
        System.out.println("GameTasks in setTaskPositions: "+gameTasks.size());
        return gameTasks;
    }
}
