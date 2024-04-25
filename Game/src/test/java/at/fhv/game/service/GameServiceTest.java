package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.GameStatus;
import at.fhv.game.model.Player;
import at.fhv.game.model.Role;
import at.fhv.game.repository.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;


    private GameService gameService;

    @Before
    public void setUp() {
        gameService = new GameService(gameRepository);
    }

    @Test
    public void createGameSuccessfully() {
        doNothing().when(gameRepository).save(any(Game.class));

        Game game = gameService.createGame(5, 1, "test_map");

        assertNotNull(game);
        assertEquals(5, game.getNumberOfPlayers());
        assertEquals(1, game.getNumberOfImpostors());
        assertEquals("test_map", game.getMap());
    }

    @Test
    public void startGameSuccessfully() {
        when(gameRepository.findByGameCode(any())).thenReturn(new Game());

        boolean gameStarted = gameService.startGame("gameCode");

        assertTrue(gameStarted);
        assertEquals(GameStatus.IN_GAME, gameRepository.findByGameCode("gameCode").getGameStatus());
    }

    @Test
    public void startGameWithInvalidCode() {
        when(gameRepository.findByGameCode(any())).thenReturn(null);

        boolean gameStarted = gameService.startGame("invalidCode");

        assertFalse(gameStarted);
    }

    @Test
    public void killPlayerSuccessfully() {
        Game game = new Game();
        List<Player> players = new ArrayList<>();
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        players.add(player);
        game.setPlayers(players);

        Mockito.when(gameRepository.findByGameCode(any())).thenReturn(game);
        doNothing().when(gameRepository).save(any(Game.class));

        game = gameService.killPlayer("gameCode", 1);

        assertEquals(Role.CREWMATE_GHOST, game.getPlayers().get(0).getRole());
    }

    @Test
    public void killPlayerWithInvalidId() {
        Game initialGame = new Game();
        List<Player> players = new ArrayList<>();
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        players.add(player);
        initialGame.setPlayers(players);

        when(gameRepository.findByGameCode(any())).thenReturn(initialGame);

        Game updatedGame = gameService.killPlayer("gameCode", 2);

        assertEquals(Role.CREWMATE, updatedGame.getPlayers().get(0).getRole());
    }

    @Test
    public void reportBodySuccessfully() {
        Game game = new Game();
        List<Player> players = new ArrayList<>();
        List<Integer> reportedBodies = new ArrayList<>();
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        players.add(player);
        game.setPlayers(players);
        game.setReportedBodies(reportedBodies);

        when(gameRepository.findByGameCode(any())).thenReturn(game);

        // Call the method to report the body
        game = gameService.reportBody("gameCode", 1);

        // Assert that the reported body ID is added to the reported bodies list
        assertEquals(1, game.getReportedBodies().size());
        assertEquals(1, (int) game.getReportedBodies().get(0)); // Assert that the reported body ID is correct
    }

    @Test
    public void testImpostorWinCondition() {
        // Create a game with 2 impostors and 1 crewmate
        Game game = new Game();
        List<Player> players = new ArrayList<>();

        Player impostor1 = new Player();
        impostor1.setId(1);
        impostor1.setRole(Role.IMPOSTOR);
        players.add(impostor1);

        Player crewmate1 = new Player();
        crewmate1.setId(2);
        crewmate1.setRole(Role.CREWMATE);
        players.add(crewmate1);

        Player crewmate2 = new Player();
        crewmate2.setId(3);
        crewmate2.setRole(Role.CREWMATE);
        players.add(crewmate2);

        game.setPlayers(players);

        // Mock the behavior of the repository method
        when(gameRepository.findByGameCode(any())).thenReturn(game);

        // Call the method to simulate killing the crewmate
        gameService.killPlayer("gameCode", 3);

        // Check if the game status is impostors win
        assertEquals(GameStatus.IMPOSTORS_WIN, game.getGameStatus());
    }
}
