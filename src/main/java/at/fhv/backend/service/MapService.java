/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          25/03/2024
 */

package at.fhv.backend.service;

import at.fhv.backend.model.Map;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    public Map map;

    public Map getMap() {
        return map;
    }

    public Map getInitialMap() {
        map = new Map();
        map.setInitialMap();
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setMapbyPosition(int x, int y, boolean value) {
        map.setMapbyPosition(x, y, value);
    }
}
