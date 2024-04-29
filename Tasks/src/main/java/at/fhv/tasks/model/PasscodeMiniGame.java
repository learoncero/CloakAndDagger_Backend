package at.fhv.tasks.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasscodeMiniGame extends MiniGame {
    private int currentSum;
    private int randomSum;
    private boolean taskDone;

    public PasscodeMiniGame(int id, String title, String description) {
        super(id, title, description);
        this.currentSum = 0;
        this.randomSum = 0;
        this.taskDone = false;
    }
}
