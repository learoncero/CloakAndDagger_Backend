package at.fhv.tasks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasscodeMiniGame extends MiniGame {
    private int currentSum;
    private int randomSum;

    public PasscodeMiniGame(int id, String title, String description) {
        super(id, title, description);
        this.currentSum = 0;
        this.randomSum = 0;
    }
}
