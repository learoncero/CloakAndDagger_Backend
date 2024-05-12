package at.fhv.minigames.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecipherSymbolsMiniGameMessage {
    private int taskId;
    private String symbol;
    private int currentRound;
}
