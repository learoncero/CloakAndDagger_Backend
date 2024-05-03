package at.fhv.tasks.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasscodeMiniGame extends MiniGame {
    @Schema(description = "The current sum of the passcode")
    private int currentSum;

    @Schema(description = "The randomly generated sum of the passcode")
    private int randomSum;

    public PasscodeMiniGame(int id, String title, String description) {
        super(id, title, description);
        this.currentSum = 0;
        this.randomSum = 0;
    }
}
