package at.fhv.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MiniGame {
    @Schema(description = "Unique identifier for the mini-game")
    private int id;

    @Schema(description = "The title of the mini-game")
    private String title;

    @Schema(description = "A brief description of the mini-game")
    private String description;

    public MiniGame(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
