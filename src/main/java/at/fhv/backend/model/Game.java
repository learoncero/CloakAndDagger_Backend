package at.fhv.backend.model;

import at.fhv.backend.service.MapService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class Game {
    private String gameCode;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private Map map;
    private List<Player> players;
    private List<Sabotage> sabotages;
    private int gameID = 0;

    public Game(String gameCode, int numberOfPlayers, int numberOfImpostors, String map, List<Player> players) {
        this.gameCode = gameCode;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = new Map();
        this.map.setInitialMap(map);
        System.out.println("Map: " + Arrays.toString(this.map.getMap()[0]));
        this.players = players;
        this.sabotages = new ArrayList<>();
        setGameID();
    }

    public Game(String gameCode, int numberOfPlayers, int numberOfImpostors, String map, MapService mapService) {
        this(gameCode, numberOfPlayers, numberOfImpostors, map, new ArrayList<>());
        mapService.setMap(mapService.getInitialMap(map).getMap());
        setGameID();
    }

    public Game() {
        this.map = new Map();
    }

    private void setGameID() {
        this.gameID = gameID + 1;
    }

    // This method is used to get the map as a 2D boolean array not an Object containing the map
    public boolean[][] getMap() {
        return map.getMap();
    }

    public void setMap(boolean[][] map) {
        this.map.setMap(map);
    }
}
