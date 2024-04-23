/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          17/04/2024
 */

package at.fhv.game.model.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BodyReportMessage {
    private String gameCode;
    private String bodyToReportId;

    public BodyReportMessage(String gameCode, String bodyToReportId) {
        this.gameCode = gameCode;
        this.bodyToReportId = bodyToReportId;
    }
}
