package at.fhv.minigames.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SortingAlgorithmMiniGame extends MiniGame {
    @Schema(description = "List of box sizes for the mini-game")
    private List<Integer> boxes = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

    @Schema(description = "Shuffled list of boxes for the mini-game")
    private List<Integer> shuffledBoxes;

    public SortingAlgorithmMiniGame(int id, String title, String description) {
        super(id, title, description);
    }
}

