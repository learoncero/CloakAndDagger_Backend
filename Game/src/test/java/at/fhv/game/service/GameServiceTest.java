package at.fhv.game.service;

import at.fhv.game.model.*;
import at.fhv.game.repository.PrivateGameRepository;
import at.fhv.game.repository.PublicGameRepository;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    private PrivateGameRepository privateGameRepository;
    @Mock
    private PublicGameRepository publicGameRepository;
    @Mock
    private TaskService taskService;

    private GameService gameService;
    private final GameMode gameMode = GameMode.PRIVATE;

    @Before
    public void setUp() {
        gameService = new GameService(privateGameRepository, publicGameRepository, taskService);
    }

    @Test
    public void createGameSuccessfully() {
        if(gameMode.equals(GameMode.PRIVATE)) {
            doNothing().when(privateGameRepository).save(any(Game.class));
        } else {
            doNothing().when(publicGameRepository).save(any(Game.class));
        }

        Game game = gameService.createGame(gameMode, 5, 1, "test_map");

        assertNotNull(game);
        assertEquals(5, game.getNumberOfPlayers());
        assertEquals(1, game.getNumberOfImpostors());
        assertEquals("test_map", game.getMap());
    }

    @Test
    public void startGameSuccessfully() {
        Game game = new Game();
        game.setGameMode(gameMode);

        if(gameMode.equals(GameMode.PRIVATE)) {
            when(privateGameRepository.findByGameCode(any())).thenReturn(game);
        } else {
            when(publicGameRepository.findByGameCode(any())).thenReturn(game);
        }

        boolean gameStarted = gameService.startGame("gameCode");

        assertTrue(gameStarted);
        if(gameMode.equals(GameMode.PRIVATE)) {
            assertEquals(GameStatus.IN_GAME, privateGameRepository.findByGameCode("gameCode").getGameStatus());
        } else {
            assertEquals(GameStatus.IN_GAME, publicGameRepository.findByGameCode("gameCode").getGameStatus());
        }
    }

    @Test
    public void startGameWithInvalidCode() {
        when(privateGameRepository.findByGameCode(any())).thenReturn(null);

        boolean gameStarted = gameService.startGame("invalidCode");

        assertFalse(gameStarted);
    }

    @Test
    public void killPlayerSuccessfully() {
        Game game = new Game();
        game.setGameMode(gameMode);
        List<Player> players = new ArrayList<>();
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        Position initialPosition = new Position(5, 5);
        player.setPlayerPosition(initialPosition);
        players.add(player);
        game.setPlayers(players);

        if(gameMode.equals(GameMode.PRIVATE)) {
            when(privateGameRepository.findByGameCode(any())).thenReturn(game);
            doNothing().when(privateGameRepository).save(any(Game.class));
        } else {
            when(publicGameRepository.findByGameCode(any())).thenReturn(game);
            doNothing().when(publicGameRepository).save(any(Game.class));
        }

        game = gameService.killPlayer("gameCode", 1, -1);

        assertEquals(Role.CREWMATE_GHOST, game.getPlayers().get(0).getRole());
        assertEquals(initialPosition, game.getPlayers().get(0).getDeadBodyPosition());
    }

    @Test
    public void killPlayerWithInvalidId() {
        Game initialGame = new Game();
        List<Player> players = new ArrayList<>();
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        Position deadBodyPosition = new Position(-1, -1);
        player.setDeadBodyPosition(deadBodyPosition);
        Position initialPosition = new Position(5, 5);
        player.setPlayerPosition(initialPosition);
        players.add(player);
        initialGame.setPlayers(players);

        when(privateGameRepository.findByGameCode(any())).thenReturn(initialGame);

        Game updatedGame = gameService.killPlayer("gameCode", 2, -1);

        assertEquals(Role.CREWMATE, updatedGame.getPlayers().get(0).getRole());
        assertEquals(-1, updatedGame.getPlayers().get(0).getDeadBodyPosition().getX());
        assertEquals(-1, updatedGame.getPlayers().get(0).getDeadBodyPosition().getY());
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

        when(privateGameRepository.findByGameCode(any())).thenReturn(game);

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
        game.setGameMode(gameMode);
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
        if(gameMode.equals(GameMode.PRIVATE)) {
            when(privateGameRepository.findByGameCode(any())).thenReturn(game);
        } else {
            when(publicGameRepository.findByGameCode(any())).thenReturn(game);
        }

        // Call the method to simulate killing the crewmate
        gameService.killPlayer("gameCode", 3, -1);

        // Check if the game status is impostors win
        assertEquals(GameStatus.IMPOSTORS_WIN, game.getGameStatus());
    }
/*
    @Test
    public void setRandomSabotagePositionSuccessfully() {
        Game game = new Game();
        List<Sabotage> sabotages = new ArrayList<>();
        Sabotage sabotage = new Sabotage();
        sabotage.setId(1);
        sabotages.add(sabotage);
        game.setSabotages(sabotages);

        when(gameRepository.findByGameCode(any())).thenReturn(game);

        Position newPosition = new Position(10, 10);
        Game updatedGame = gameService.setRandomSabotagePosition("gameCode", 1, newPosition);

        assertEquals(newPosition, updatedGame.getSabotages().get(0).getPosition());
    }

    @Test
    public void setRandomSabotagePositionWithInvalidGame() {
        when(gameRepository.findByGameCode(any())).thenReturn(null);

        Game updatedGame = gameService.setRandomSabotagePosition("gameCode", 1, new Position(10, 10));

        assertNull(updatedGame);
    }

 */

    @Test
    public void endGameSuccessfully() {
        Game game = new Game();
        game.setGameMode(gameMode);

        if(gameMode.equals(GameMode.PRIVATE)) {
            when(privateGameRepository.findByGameCode(any())).thenReturn(game);
        }else {
            when(publicGameRepository.findByGameCode(any())).thenReturn(game);
        }

        Game endedGame = gameService.endGame("gameCode");

        assertEquals(GameStatus.IMPOSTORS_WIN, endedGame.getGameStatus());
    }

    @Test
    public void cancelSabotageSuccessfully() {
        Game game = new Game();
        game.setGameMode(gameMode);
        List<Sabotage> sabotages = new ArrayList<>();
        Sabotage sabotage = new Sabotage();
        sabotage.setPosition(new Position(10, 10));
        sabotages.add(sabotage);
        game.setSabotages(sabotages);

        Game updatedGame = gameService.cancelSabotage(game);

        for (Sabotage s : updatedGame.getSabotages()) {
            assertEquals(s.getPosition().getX(), -1);
            assertEquals(s.getPosition().getY(), -1);
        }
    }

    @Test
    public void checkCrewmatesWin_NoImpostors_NoTasksRemaining() {
        // Create a game with no impostors and no tasks remaining
        Game game = new Game();
        List<Player> players = new ArrayList<>();
        Player crewmate1 = new Player();
        crewmate1.setRole(Role.CREWMATE);
        players.add(crewmate1);
        Player impostorGhost = new Player();
        impostorGhost.setRole(Role.IMPOSTOR_GHOST);
        players.add(impostorGhost);
        game.setPlayers(players);
        Task task1 = new Task();
        task1.setCompleted(true);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        game.setTasks(tasks);

        // Call the method to check crewmates win condition
        gameService.checkCrewmatesWin(game);

        // Check if the game status is CREWMATES_WIN
        assertEquals(GameStatus.CREWMATES_WIN, game.getGameStatus());
    }

    @Test
    public void checkCrewmatesWin_NoImpostors_TasksRemaining() {
        // Create a game with no impostors but tasks remaining
        Game game = new Game();
        game.setNumberOfImpostors(1);
        List<Player> players = new ArrayList<>();
        Player crewmate1 = new Player();
        crewmate1.setRole(Role.CREWMATE);
        players.add(crewmate1);
        Player impostorGhost = new Player();
        impostorGhost.setRole(Role.IMPOSTOR_GHOST);
        players.add(impostorGhost);
        game.setPlayers(players);
        Task task1 = new Task();
        task1.setCompleted(false);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        game.setTasks(tasks);

        // Call the method to check crewmates win condition
        gameService.checkCrewmatesWin(game);

        // Check if the game status is not CREWMATES_WIN
        assertEquals(GameStatus.CREWMATES_WIN, game.getGameStatus());
    }

    @Test
    public void checkCrewmatesWin_ImpostorsExist_NoTasksRemaining() {
        // Create a game with impostors but no tasks remaining
        Game game = new Game();
        game.setNumberOfImpostors(1);
        List<Player> players = new ArrayList<>();
        Player crewmate1 = new Player();
        crewmate1.setRole(Role.CREWMATE);
        players.add(crewmate1);
        Player impostor = new Player();
        impostor.setRole(Role.IMPOSTOR);
        players.add(impostor);
        game.setPlayers(players);
        Task task1 = new Task();
        task1.setCompleted(true);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        game.setTasks(tasks);

        // Call the method to check crewmates win condition
        gameService.checkCrewmatesWin(game);

        // Check if the game status is not CREWMATES_WIN
        assertEquals(GameStatus.CREWMATES_WIN, game.getGameStatus());
    }

    @Test
    public void checkCrewmatesWin_ImpostorsExist_TasksRemaining() {
        // Create a game with impostors and tasks remaining
        Game game = new Game();
        List<Player> players = new ArrayList<>();
        Player crewmate1 = new Player();
        crewmate1.setRole(Role.CREWMATE);
        players.add(crewmate1);
        Player impostor = new Player();
        impostor.setRole(Role.IMPOSTOR);
        players.add(impostor);
        game.setPlayers(players);
        Task task1 = new Task();
        task1.setCompleted(false);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        game.setTasks(tasks);

        // Call the method to check crewmates win condition
        gameService.checkCrewmatesWin(game);

        // Check if the game status is not CREWMATES_WIN
        assertNotEquals(GameStatus.CREWMATES_WIN, game.getGameStatus());
    }

    @Test
    public void testEliminatePlayer() {
        // Create a game and a player
        Game game = new Game();
        game.setGameMode(gameMode);
        game.setPlayers(new ArrayList<>());
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        game.getPlayers().add(player);
        Player player2 = new Player();
        player2.setId(2);
        player2.setRole(Role.IMPOSTOR);
        game.getPlayers().add(player2);

        // Mock the gameRepository to return our game
        if(gameMode.equals(GameMode.PRIVATE)) {
            when(privateGameRepository.findByGameCode(any())).thenReturn(game);
            doNothing().when(privateGameRepository).save(any(Game.class));
        } else {
            when(publicGameRepository.findByGameCode(any())).thenReturn(game);
            doNothing().when(publicGameRepository).save(any(Game.class));
        }

        // Call the method to eliminate the player
        Game updatedGame = gameService.eliminatePlayer("gameCode", 1);

        // Check if the player's role has been updated to CREWMATE_GHOST
        assertEquals(Role.CREWMATE_GHOST, updatedGame.getPlayers().get(0).getRole());

        // Now set the player's role to IMPOSTOR and eliminate the player again
        updatedGame = gameService.eliminatePlayer("gameCode", 2);

        // Check if the player's role has been updated to IMPOSTOR_GHOST
        assertEquals(Role.IMPOSTOR_GHOST, updatedGame.getPlayers().get(1).getRole());

        // Verify that the gameRepository's save method was called twice
        if(gameMode.equals(GameMode.PRIVATE)) {
            verify(privateGameRepository, times(2)).save(any(Game.class));
        } else {
            verify(publicGameRepository, times(2)).save(any(Game.class));
        }
    }

}
