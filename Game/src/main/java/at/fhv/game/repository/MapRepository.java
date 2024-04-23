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

        char[][] devMap1Array = MapLoader.loadMapFromFile("DevMap1");
        Map devMap1 = new Map("DevMap1", devMap1Array);
        maps.add(devMap1);

        char[][] devMap2Array = MapLoader.loadMapFromFile("DevMap2");
        Map devMap2 = new Map("DevMap2", devMap2Array);
        maps.add(devMap2);
    }
}
