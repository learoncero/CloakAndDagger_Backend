/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          25/03/2024
 */

package at.fhv.backend.service;

import at.fhv.backend.model.Map;
import at.fhv.backend.model.Position;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MapService {

    public Map map;

    public MapService() {
    }

    public MapService(String mapName) {
        getInitialMap(mapName);
    }

    public boolean[][] getMap(){
        return map.getMap();
    }

    public Map getInitialMap(String mapName) {
        map = new Map();
        map.setInitialMap(mapName);
        return map;
    }

    public void setMap(boolean[][] map) {
        this.map.setMap(map);
    }

    public void setMapbyPosition(int x, int y, boolean value) {
        map.setMapbyPosition(x, y, value);
    }

    public boolean isCellWalkable(int x, int y) {
        return map.getCellValue(x, y);
    }
    public List<Position> loadWalkablePositions() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/main/java/at/fhv/backend/repository/map2.txt"));
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

    public Position getRandomWalkablePosition() throws Exception {
        List<Position> walkablePositions = loadWalkablePositions();
        if (!walkablePositions.isEmpty()) {
            Random random = new Random();
            return walkablePositions.get(random.nextInt(walkablePositions.size()));
        } else {
            throw new Exception("Keine begehbaren Positionen gefunden.");
        }
    }


    public static void main(String[] args) {
        //Todo - into unit test

        /*MapService mapService = new MapService();
        Map map = mapService.getInitialMap();
        boolean[][] array = map.getMap();
        int arrayRowlength = array[0].length;
        int y =0;
        while(y < map.getMap().length) {
            for (int x = 0; x < arrayRowlength; x++) {
                if (map.getCellValue(x, y)) {
                    System.out.print(".");
                } else {
                    System.out.print("#");
                }
            }
            System.out.println();
            y++;
        }
        System.out.println(map.getCellValue(7,1));*/
    }
}
