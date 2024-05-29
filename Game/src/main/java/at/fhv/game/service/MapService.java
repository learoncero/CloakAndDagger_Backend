package at.fhv.game.service;

import at.fhv.game.model.Map;
import at.fhv.game.model.Position;
import at.fhv.game.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MapService {
    private final MapRepository mapRepository;

    @Autowired
    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public Map getMapByName(String mapName) {
        return mapRepository.findMapByName(mapName);
    }

    private List<String> readResourceFile(String mapName) throws IOException {
        String resourcePath = "at/fhv/game/repository/" + mapName.toLowerCase() + ".txt";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.toList());
            }
        }
    }

    public List<Position> loadWalkablePositions(String mapName) throws IOException {
        List<String> lines = readResourceFile(mapName);
        List<Position> walkablePositions = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '.') {
                    walkablePositions.add(new Position(x, y));
                }
            }
        }
        return walkablePositions;
    }

    public Position getRandomWalkablePosition(String mapName) throws IOException {
        List<Position> walkablePositions = loadWalkablePositions(mapName);
        if (!walkablePositions.isEmpty()) {
            Random random = new Random();
            return walkablePositions.get(random.nextInt(walkablePositions.size()));
        } else {
            throw new IOException("No walkable positions found.");
        }
    }

    public List<Position> getTaskPositions(String mapName) throws IOException {
        List<String> lines = readResourceFile(mapName);
        List<Position> taskPositions = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'T') {
                    taskPositions.add(new Position(x, y));
                }
            }
        }
        return taskPositions;
    }
}
