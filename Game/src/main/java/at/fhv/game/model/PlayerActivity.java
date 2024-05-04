package at.fhv.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerActivity {
    @Schema(description = "The timestamp of the last move made by the player")
    private long lastMoveTime;

    @Schema(description = "The game code associated with the player's activity")
    private String gameCode;

    public PlayerActivity(long lastMoveTime, String gameCode) {
        this.lastMoveTime = lastMoveTime;
        this.gameCode = gameCode;
    }
}
