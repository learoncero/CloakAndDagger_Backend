package at.fhv.minigames.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovingSquareMiniGame extends MiniGame {

    public MovingSquareMiniGame(int id, String title, String description) {
        super(id, title, description);
    }
}
