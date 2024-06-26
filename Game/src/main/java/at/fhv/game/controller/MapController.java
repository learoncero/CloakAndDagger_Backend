package at.fhv.game.controller;

import at.fhv.game.model.Map;
import at.fhv.game.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get map by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Map found"),
            @ApiResponse(responseCode = "404", description = "Map not found")
    })
    @GetMapping("/map/{mapName}")
    public ResponseEntity<Map> getMap(@PathVariable String mapName) {
        Map map = mapService.getMapByName(mapName);

        return ResponseEntity.ok(map);
    }
}
