package at.fhv.tasks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ColorSeqMiniGame extends MiniGame {
    private List<String> colors = Arrays.asList("#f00", "#00f", "#0f0", "#ff0");
    private List<String> shuffledColors;
    private boolean isEqual;

    public ColorSeqMiniGame(int id, String title, String description) {
        super(id, title, description);
        this.isEqual = false;

    }



}
