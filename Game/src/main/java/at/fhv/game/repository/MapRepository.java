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
        char[][] spaceshipMapArray = MapLoader.loadMapFromFile("spaceship");
        Map spaceshipMap = new Map("Spaceship", spaceshipMapArray);
        maps.add(spaceshipMap);

        char[][] wormholeMapArray = MapLoader.loadMapFromFile("wormhole");
        Map wormholeMap = new Map("Wormhole", wormholeMapArray);
        maps.add(wormholeMap);

        char[][] jungleMapArray = MapLoader.loadMapFromFile("jungle");
        Map jungleMap = new Map("Jungle", jungleMapArray);
        maps.add(jungleMap);

        char[][] basementMapArray = MapLoader.loadMapFromFile("basement");
        Map basementMap = new Map("Basement", basementMapArray);
        maps.add(basementMap);
    }
}
