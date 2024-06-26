package at.fhv.game.repository;

import at.fhv.game.model.Map;
import at.fhv.game.utils.MapLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapRepository {
    List<Map> maps;

    @Autowired
    public MapRepository(MapLoader mapLoader) {
        maps = new ArrayList<>();
        initializeMaps();
    }

    public Map findMapByName(String mapName) {
        return maps.stream()
                .filter(map -> map.getName().equals(mapName))
                .findFirst()
                .orElse(null);
    }

    private void initializeMaps() {
        char[][] spaceshipMapArray = MapLoader.loadMapFromFile("Spaceship");
        Map spaceshipMap = new Map("Spaceship", spaceshipMapArray);
        maps.add(spaceshipMap);

        char[][] pirateShipMapArray = MapLoader.loadMapFromFile("PirateShip");
        Map pirateShipMap = new Map("PirateShip", pirateShipMapArray);
        maps.add(pirateShipMap);

        char[][] jungleMapArray = MapLoader.loadMapFromFile("Jungle");
        Map jungleMap = new Map("Jungle", jungleMapArray);
        maps.add(jungleMap);

        char[][] basementMapArray = MapLoader.loadMapFromFile("Basement");
        Map basementMap = new Map("Basement", basementMapArray);
        maps.add(basementMap);
    }
}
