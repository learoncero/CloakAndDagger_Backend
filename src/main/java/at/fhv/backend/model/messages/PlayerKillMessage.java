/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          17/04/2024
 */

package at.fhv.backend.model.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerKillMessage {
    private String gameCode;
    private String playerToKillId;

    public PlayerKillMessage(String gameCode, String playerToKillId) {
        this.gameCode = gameCode;
        this.playerToKillId = playerToKillId;
    }
}
