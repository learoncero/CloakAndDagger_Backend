/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          25/03/2024
 */

package at.fhv.backend.controller;

import at.fhv.backend.model.Map;
import at.fhv.backend.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MapController {
    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @MessageMapping("/mapinitialiser")
    @SendTo("/topic/mapinitialiser")
    public Map getInitialMap() {
        return mapService.getInitialMap();
    }

    @MessageMapping("/mapupdate")
    @SendTo("/topic/mapupdate")
    public Map updateMap(Map map) {
        mapService.setMap(map);
        return map;
    }

    @MessageMapping("/mapupdatebyposition")
    @SendTo("/topic/mapupdatebyposition")
    public Map updateMapByPosition(int x, int y, boolean value) {
        mapService.setMapbyPosition(x, y, value);
        return mapService.getMap();
    }
}
