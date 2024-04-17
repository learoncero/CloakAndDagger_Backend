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
import at.fhv.backend.repository.MapRepository;
import org.springframework.stereotype.Service;

@Service
public class MapService {
    private final MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public boolean[][] getMap(String mapName) {
        return mapRepository.findMapByName(mapName);
    }

    public boolean isCellWalkable(String mapName, int x, int y) {
        boolean[][] map = mapRepository.findMapByName(mapName);
        if (map.length <= y || map[0].length <= x) {
            return false;
        }
        return true;
    }
}
