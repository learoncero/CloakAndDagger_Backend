package at.fhv.minigames.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortingAlgorithmMiniGameMessage {
    private int taskId;
    private List<Integer> boxes;
}

