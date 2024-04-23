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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

        assertEquals(true, gameStarted);
        assertEquals(GameStatus.IN_GAME, gameRepository.findByGameCode("gameCode").getGameStatus());
    }

    @Test
    public void startGameWithInvalidCode() {
        when(gameRepository.findByGameCode(any())).thenReturn(null);

        boolean gameStarted = gameService.startGame("invalidCode");

        assertEquals(false, gameStarted);
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
}
