/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          17/05/2024
 */

package at.fhv.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VoteEvent {

    @Schema(description = "Player id for Player that got voted")
    private Integer votedForPlayer;

    @Schema(description = "Player id for Player that cast a vote")
    private Integer votedBy;

    public VoteEvent(Integer votedForPlayer, Integer votedBy) {
        this.votedForPlayer = votedForPlayer;
        this.votedBy = votedBy;
    }
}
