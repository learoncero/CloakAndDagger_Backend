package at.fhv.backend.model;

import at.fhv.backend.service.MapService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Game {
    private static int nextGameID = 1;
    private String gameCode;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private Map map;
    private List<Player> players;
    private List<Sabotage> sabotages;
    private int gameID;
    private GameStatus gameStatus;

    // This constructor is used to start a game
    public Game(String gameCode, int numberOfPlayers, int numberOfImpostors, boolean[][] map, List<Player> players, List<Sabotage> sabotages, int gameID, GameStatus gameStatus) {
        this.gameCode = gameCode;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = new Map();
        this.map.setMap(map);
        this.players = players;
        this.sabotages = sabotages;
        this.gameID = gameID;
        this.gameStatus = gameStatus;
    }

    // This constructor is used to create a game
    public Game(String gameCode, int numberOfPlayers, int numberOfImpostors, String mapString, MapService mapService) {
        Map map = new Map();
        map.setInitialMap(mapString);
        this(gameCode, numberOfPlayers, numberOfImpostors, map, new ArrayList<>(), new ArrayList<>(), nextGameID++, GameStatus.NOT_FINISHED);
        mapService.setMap(mapService.getInitialMap(map).getMap());
    }

    public Game() {
        this.map = new Map();
    }

    // This method is used to get the map as a 2D boolean array not an Object containing the map
    public boolean[][] getMap() {
        return map.getMap();
    }

    public void setMap(boolean[][] map) {
        this.map.setMap(map);
    }
}
