package at.fhv.game.model;

import at.fhv.game.utils.GameCodeGenerator;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Unique code identifying the game", example = "ABC123")
    private String gameCode;

    @Schema(description = "Number of players in the game", example = "6")
    private int numberOfPlayers;

    @Schema(description = "Number of impostors in the game", example = "2")
    private int numberOfImpostors;

    @Schema(description = "Map used in the game", example = "Spaceship")
    private String map;

    @Schema(description = "List of players in the game")
    private List<Player> players;

    @Schema(description = "List of tasks in the game")
    private List<Task> tasks;

    @Schema(description = "List of sabotages in the game")
    private List<Sabotage> sabotages;

    @Schema(description = "Unique identifier for the game", example = "1")
    private int gameID;

    @Schema(description = "Status of the game", example = "LOBBY")
    private GameStatus gameStatus;

    @Schema(description = "List of reported bodies in the game")
    private List<Integer> reportedBodies;

    @Schema(description = "List of eliminated players in the game, -1=tie, 0=no votes")
    private List<Integer> votingResults;

    public Game(int numberOfPlayers, int numberOfImpostors, String map) {
        this.gameCode = GameCodeGenerator.generateGameCode();
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;
        this.players = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.sabotages = new ArrayList<>();
        this.gameID = nextGameID;
        this.gameStatus = GameStatus.LOBBY;
        this.reportedBodies = new ArrayList<>();
        this.votingResults = new ArrayList<>();
    }

    public Game() {
    }
}