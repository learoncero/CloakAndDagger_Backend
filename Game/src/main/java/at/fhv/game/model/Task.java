package at.fhv.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private static int nextTaskID = 1;
    @Schema(description = "The ID of the task")
    private int taskId;

    @Schema(description = "The ID of the associated mini-game")
    private int miniGameId;

    @Schema(description = "The title of the task")
    private String title;

    @Schema(description = "The description of the task")
    private String description;

    @Schema(description = "Flag indicating if the task is completed")
    private boolean isCompleted;

    @Schema(description = "The position of the task")
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
