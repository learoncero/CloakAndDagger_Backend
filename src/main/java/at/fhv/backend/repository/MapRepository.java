package at.fhv.backend.repository;

import at.fhv.backend.model.Map;
import at.fhv.backend.utils.MapLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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
        boolean[][] spaceshipMap = MapLoader.loadMapFromFile("spaceship");
        Map map = new Map("Spaceship", spaceshipMap);
        maps.add(map);
    }
}
