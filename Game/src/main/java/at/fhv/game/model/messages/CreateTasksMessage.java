package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTasksMessage {
    private String gameCode;

    public CreateTasksMessage(String gameCode) {
        this.gameCode = gameCode;
    }
}
