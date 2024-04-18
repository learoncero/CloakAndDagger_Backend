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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService {
    private final MapRepository mapRepository;

    @Autowired
    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public Map getMap(String mapName) {
        return mapRepository.findMapByName(mapName);
    }

    public boolean isCellWalkable(String mapName, int x, int y) {
        Map map = mapRepository.findMapByName(mapName);
        return map.getCellValue(x, y);
    }
}
