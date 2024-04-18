package at.fhv.backend.service;/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          16/04/2024
 */

import at.fhv.backend.model.Game;
import at.fhv.backend.model.Map;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Role;
import at.fhv.backend.repository.GameRepository;
import at.fhv.backend.service.GameService;
import at.fhv.backend.service.MapService;
import at.fhv.backend.service.PlayerService;
import at.fhv.backend.service.SabotageService;
import at.fhv.backend.utils.MapLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private MapService mapService;

    @Mock
    private SabotageService sabotageService;

    private GameService gameService;

    @Before
    public void setUp() {
        gameService = new GameService(gameRepository, playerService, mapService, sabotageService);
    }

    @Test
    public void createGameSuccessfully() throws IOException {
        Player player = new Player();
        //when(playerService.createPlayer(anyString(), any(), any())).thenReturn(player);
//        when(mapService.getInitialMap(anyString())).thenReturn(new Map());
        doAnswer(i -> i.getArguments()[0]).when(gameRepository).save(any(Game.class));

        // Create a temporary file and write test data to it
        String testMapFileName = "testmap";
        String testMapContent = ".#..\n" +
                "..#.\n" +
                "....";
        String testMapFilePath = "src/main/java/at/fhv/backend/repository/" + testMapFileName + ".txt";
        createTestMapFile(testMapFilePath, testMapContent);

        // Load the map from the test file
        boolean[][] loadedMap = MapLoader.loadMapFromFile(testMapFileName);

        Game game = gameService.createGame(player, 5, 1, testMapFileName);

        assertNotNull(game);
        assertEquals(5, game.getNumberOfPlayers());
        // Delete the temporary file
        // Clean up the test file
        deleteTestMapFile(testMapFilePath);
    }

    private void createTestMapFile(String filePath, String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to delete the test map file
    private void deleteTestMapFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void startGameSuccessfully() {
        Game game = new Game();
        when(gameRepository.findByGameCode(anyString())).thenReturn(game);
        doAnswer(i -> i.getArguments()[0]).when(gameRepository).save(any(Game.class));

        Game startedGame = gameService.startGame("gameCode");

        assertEquals(game, startedGame);
    }

    @Test
    public void startGameWithInvalidCode() {
        when(gameRepository.findByGameCode(anyString())).thenReturn(null);

        Game startedGame = gameService.startGame("invalidCode");

        assertNull(startedGame);
    }

    @Test
    public void setGameAttributesSuccessfully() {
        Game game = new Game();
        List<Player> players = new ArrayList<>();
        players.add(new Player());
        when(gameRepository.findByGameCode(anyString())).thenReturn(game);
        doAnswer(i -> i.getArguments()[0]).when(gameRepository).save(any(Game.class));

        Game updatedGame = gameService.setGameAttributes("gameCode", players);

        assertEquals(game, updatedGame);
        assertEquals(players, updatedGame.getPlayers());
    }

    @Test
    public void killPlayerSuccessfully() {
        Game game = new Game();
        game.setPlayers(new ArrayList<>());
        Player player = new Player();
        player.setId(1);
        player.setRole(Role.CREWMATE);
        game.getPlayers().add(player);
        when(gameRepository.findByGameCode(anyString())).thenReturn(game);
        doAnswer(i -> i.getArguments()[0]).when(gameRepository).save(any(Game.class));

        Game updatedGame = gameService.killPlayer("gameCode", 1);

        assertEquals(game, updatedGame);
        assertEquals(Role.CREWMATE_GHOST, updatedGame.getPlayers().get(0).getRole());
    }

    @Test
    public void killPlayerWithInvalidId() {
        Game game = new Game();
        game.setPlayers(new ArrayList<>());
        when(gameRepository.findByGameCode(anyString())).thenReturn(game);

        Game updatedGame = gameService.killPlayer("gameCode", 1);

        assertNull(updatedGame);
    }
}
