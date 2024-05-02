package at.fhv.tasks.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColorSeqMiniGameMessage {
    private int taskId;
    private List<String> colors;

    private List<String> shuffledColors;

}
