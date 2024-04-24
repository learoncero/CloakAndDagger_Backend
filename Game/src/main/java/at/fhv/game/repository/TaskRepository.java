/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          24/04/2024
 */

package at.fhv.game.repository;
import at.fhv.game.model.Task;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskRepository {
    private final List<Task> tasks;

    public TaskRepository() {
        this.tasks = new ArrayList<>();
        initialiseTasks();
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    private void initialiseTasks() {
        tasks.add(new Task(1, "Connect cables", "Enables electricity supply to critical systems"));
        tasks.add(new Task(2, "Updating system", "Updates data und ensures smooth operations"));
        tasks.add(new Task(3, "Enable security cameras", "Makes sure surveillance is up and running"));
        /*tasks.add(new Task(4, "Connect network", "Enables devices to connect to each other"));
        tasks.add(new Task(5, "Store tools in right place", "Keep tools safe from being damaged"));*/
    }
}