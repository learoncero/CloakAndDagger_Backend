package at.fhv.backend.model;

import at.fhv.backend.repository.PasscodeTaskRepository;
import at.fhv.backend.utils.GameCodeGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.scheduling.config.Task;

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
    }

    public Game() {
    }
}