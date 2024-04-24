/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          24/04/2024
 */

package at.fhv.game.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Task {
    private static int nextTaskID = 1;
    private int taskId;
    private int miniGameId;
    private String title;
    private String description;
    private boolean isCompleted;
    private Position position;

    public Task(int id, String title, String description) {
        this.taskId = nextTaskID;
        this.miniGameId = id;
        this.title = title;
        this.description = description;
        this.isCompleted = false;
        nextTaskID++;
    }

    public Task() {
    }
}
