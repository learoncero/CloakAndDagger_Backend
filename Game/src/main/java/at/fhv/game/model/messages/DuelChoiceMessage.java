package at.fhv.game.model.messages;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuelChoiceMessage {
    private String gameCode;
    private String choice;

    public DuelChoiceMessage(String gameCode, String choice) {
        this.gameCode = gameCode;
        this.choice = choice;
    }

    public DuelChoiceMessage() {
    }
}
