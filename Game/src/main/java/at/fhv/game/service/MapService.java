package at.fhv.game.service;

import at.fhv.game.model.Map;
import at.fhv.game.model.Pair;
import at.fhv.game.model.Position;
import at.fhv.game.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String resourcePath = "at/fhv/game/repository/" + mapName + ".txt";
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

    public List<Position[]> findPossibleWallPositions(String mapName) throws IOException {
        List<String> lines = readResourceFile(mapName);
        List<Position> walkablePositions = loadWalkablePositions(mapName);
        List<Position[]> possibleWalls = new ArrayList<>();

        for (Position pos : walkablePositions) {
            int x = pos.getX();
            int y = pos.getY();

            // Check horizontal placement
            if (isWalkable(lines, x, y) && isWalkable(lines, x + 1, y)
                    && isWallOrBoundary(lines, x - 1, y) && isWallOrBoundary(lines, x + 2, y)
                    && !hasAdjacentWalkableLine(lines, x, y, true)) {
                possibleWalls.add(new Position[]{new Position(x, y), new Position(x + 1, y)});
            }

            // Check vertical placement
            if (isWalkable(lines, x, y) && isWalkable(lines, x, y + 1)
                    && isWallOrBoundary(lines, x, y - 1) && isWallOrBoundary(lines, x, y + 2)
                    && !hasAdjacentWalkableLine(lines, x, y, false)) {
                possibleWalls.add(new Position[]{new Position(x, y), new Position(x, y + 1)});
            }
        }

        return possibleWalls;
    }

    public Position[] getRandomWallPosition(String mapName) throws Exception {
        List<Position[]> possibleWallPositions = findPossibleWallPositions(mapName);
        if (!possibleWallPositions.isEmpty()) {
            Random random = new Random();
            return possibleWallPositions.get(random.nextInt(possibleWallPositions.size()));
        } else {
            throw new Exception("No possible wall positions found.");
        }
    }

    private boolean isWalkable(List<String> lines, int x, int y) {
        return y >= 0 && y < lines.size() && x >= 0 && x < lines.get(y).length() && lines.get(y).charAt(x) == '.';
    }

    private boolean isWallOrBoundary(List<String> lines, int x, int y) {
        return y < 0 || y >= lines.size() || x < 0 || x >= lines.get(y).length() || lines.get(y).charAt(x) == '#';
    }

    private boolean hasAdjacentWalkableLine(List<String> lines, int x, int y, boolean horizontal) {
        if (horizontal) {
            return (isWalkable(lines, x - 1, y) && isWalkable(lines, x - 2, y))
                    && (isWalkable(lines, x + 2, y) && isWalkable(lines, x + 3, y));
        } else {
            return (isWalkable(lines, x, y - 1) && isWalkable(lines, x, y - 2))
                    && (isWalkable(lines, x, y + 2) && isWalkable(lines, x, y + 3));
        }
    }

    public List<Pair<Position>> getVentPositions(String mapName) {
        return getMapByName(mapName).getVentPositions();
    }

}
