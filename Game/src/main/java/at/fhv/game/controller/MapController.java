package at.fhv.game.controller;

import at.fhv.game.model.Map;
import at.fhv.game.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class MapController {
    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/map/{mapName}")
    public ResponseEntity<Map> getMap(@PathVariable String mapName) {
        Map map = mapService.getMapByName(mapName);

        System.out.println("Get map called: " + map.getName());
        return ResponseEntity.ok(map);
    }
}
