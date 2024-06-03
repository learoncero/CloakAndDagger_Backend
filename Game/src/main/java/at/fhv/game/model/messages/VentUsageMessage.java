/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          03/06/2024
 */

package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VentUsageMessage {
    private String gameCode;
    private int playerId;

    public VentUsageMessage(String gameCode, int id) {
        this.gameCode = gameCode;
        this.playerId = id;
    }
}
