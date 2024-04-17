package at.fhv.backend.repository;

import at.fhv.backend.model.Map;
import at.fhv.backend.utils.MapLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MapRepository {
    private final MapLoader mapLoader;
    List<Map> maps;

    @Autowired
    public MapRepository(MapLoader mapLoader) {
        maps = new ArrayList<>();
        this.mapLoader = mapLoader;
        initializeMaps();
    }

    public boolean[][] findMapByName(String mapName) {
        return maps.stream()
                .filter(map -> map.getName().equals(mapName))
                .findFirst()
                .map(Map::getMap)
                .orElse(null);
    }

    private void initializeMaps() {
        boolean[][] spaceshipMap = mapLoader.loadMapFromFile("spaceship");
        Map map = new Map();
        map.setName("Spaceship");
        map.setMap(spaceshipMap);
        maps.add(map);
    }
}
