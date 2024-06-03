/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          03/06/2024
 */

package at.fhv.game.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair <Position> {
    private Position first;
    private Position second;

    public Pair(Position first, Position second) {
        this.first = first;
        this.second = second;
    }

    public void add(Position position) {
        if (first == null) {
            first = position;
        } else if (second == null) {
            second = position;
        }
    }
}
