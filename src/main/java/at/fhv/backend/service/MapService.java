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
import org.springframework.stereotype.Service;

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
