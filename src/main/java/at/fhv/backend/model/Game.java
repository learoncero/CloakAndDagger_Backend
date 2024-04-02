package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Game {
    private String gameCode;
    private int numberOfPlayers;
    private int numberOfImpostors;
    private String map;
    private List<Player> players;
    private static int nextID = 1;

    public Game(String gameCode, int numberOfPlayers, int numberOfImpostors, String map, List<Player> players) {
        this.gameCode = gameCode;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;
        this.players = players;
    }

    public Game(String gameCode, int numberOfPlayers, int numberOfImpostors, String map) {
        this(gameCode, numberOfPlayers, numberOfImpostors, map, new ArrayList<>());
    }

    public Game() {}

    public static void setNextID(int nextID) {
        Game.nextID = nextID;
    }
}
