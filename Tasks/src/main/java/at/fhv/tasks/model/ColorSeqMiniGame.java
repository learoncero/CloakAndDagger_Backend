package at.fhv.tasks.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ColorSeqMiniGame extends MiniGame {
    @Schema(description = "List of colors for the mini-game")
    private List<String> colors = Arrays.asList("#f00", "#00f", "#0f0", "#ff0");

    @Schema(description = "Shuffled list of colors for the mini-game")
    private List<String> shuffledColors;

    @Schema(description = "Flag indicating if the colors are equal")
    private boolean isEqual;

    public ColorSeqMiniGame(int id, String title, String description) {
        super(id, title, description);
        this.isEqual = false;

    }


}
