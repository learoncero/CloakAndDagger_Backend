package at.fhv.tasks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ColorSeqMiniGame extends MiniGame {
    private List<String> colors;
    private List<String> shuffledColors;

    public ColorSeqMiniGame(int id, String title, String description) {
        super(id, title, description);
    }



}
