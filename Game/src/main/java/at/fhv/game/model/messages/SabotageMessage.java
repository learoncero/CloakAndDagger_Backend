/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          25/04/2024
 */

package at.fhv.game.model.messages;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SabotageMessage {
    private String gameCode;
    private String sabotageId;
    private String map;

    public SabotageMessage(String gameCode, String sabotageId, String map) {
        this.gameCode = gameCode;
        this.sabotageId = sabotageId;
        this.map = map;
    }

    public SabotageMessage() {
    }
}
