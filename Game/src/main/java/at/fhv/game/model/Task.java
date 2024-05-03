package at.fhv.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private static int nextTaskID = 1;
    private int taskId;
    private int miniGameId;
    private String title;
    private String description;
    private boolean isCompleted;
    private Position position;
    private boolean isActive;

    public Task(int id, String title, String description) {
        this.taskId = nextTaskID++;
        this.miniGameId = id;
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }
}
