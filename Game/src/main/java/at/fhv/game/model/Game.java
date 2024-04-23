package at.fhv.game.model;

import at.fhv.game.utils.GameCodeGenerator;
import at.fhv.game.repository.PasscodeTaskRepository;
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
    private String map;
    private List<Player> players;
    private PasscodeTask passcodeTask;
    private List<Sabotage> sabotages;
    private int gameID;
    private GameStatus gameStatus;
    private List<Integer> reportedBodies;

    public Game(int numberOfPlayers, int numberOfImpostors, String map) {
        this.gameCode = GameCodeGenerator.generateGameCode();
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;
        this.players = new ArrayList<>();
        this.passcodeTask = new PasscodeTask();
        this.sabotages = new ArrayList<>();
        this.gameID = nextGameID;
        this.gameStatus = GameStatus.LOBBY;
        this.reportedBodies = new ArrayList<>();
    }

    public Game() {
    }
}