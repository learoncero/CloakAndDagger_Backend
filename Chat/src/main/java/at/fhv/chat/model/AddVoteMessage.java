package at.fhv.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddVoteMessage {
    private String gameCode;
    private int playerId;
}
