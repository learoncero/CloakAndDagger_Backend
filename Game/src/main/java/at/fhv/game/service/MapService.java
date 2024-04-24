package at.fhv.game.service;

import at.fhv.game.model.Map;
import at.fhv.game.model.Position;
import at.fhv.game.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public List<Position> loadWalkablePositions(String mapName) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("Game/src/main/java/at/fhv/game/repository/" + mapName + ".txt"));
        List<Position> walkablePositions = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) != '#') {
                    walkablePositions.add(new Position(x, y));
                }
            }
        }
        return walkablePositions;
    }

    public Position getRandomWalkablePosition(String mapName) throws Exception {
        List<Position> walkablePositions = loadWalkablePositions(mapName);
        if (!walkablePositions.isEmpty()) {
            Random random = new Random();
            return walkablePositions.get(random.nextInt(walkablePositions.size()));
        } else {
            throw new Exception("No walkable positions found.");
        }
    }

    public List<Position> getTaskPositions(String mapName) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("Game/src/main/java/at/fhv/game/repository/" + mapName + ".txt"));
        List<Position> taskPositions = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'T') {
                    taskPositions.add(new Position(x, y));
                }
            }
        }
        /*System.out.println("Task positions: ");
        for (Position p: taskPositions){
            System.out.println("x: "+p.getX() + ", " +"y: "+ p.getY());
        }*/
        return taskPositions;
    }
}
