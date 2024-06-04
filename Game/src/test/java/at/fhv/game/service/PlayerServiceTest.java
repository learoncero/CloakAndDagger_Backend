/*
 * Copyright (c) 2024 Sarah N
 *
 * Project Name:         AmongUs_Replica_Backend
 * Description:
 *
 * Date of Creation/
 * Last Update:          03/06/2024
 */

package at.fhv.game.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.fhv.game.model.*;
import at.fhv.game.model.Pair;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {
    private PlayerService playerService;
    private Game game;
    private char[][] testMapContent_charArr;

    @BeforeEach
    public void setup() {
        playerService = new PlayerService();
        game = new Game(); // Initialize this as per your game setup
        this.testMapContent_charArr = new char[][]{
                {'.', '#', '.', '.'},
                {'.', '.', '#', '.'},
                {'.', '.', 'T', '.'}
        };
    }

    @Test
    public void testCreatePlayer() {
        Player player = playerService.createPlayer("testPlayer", new Position(1, 1), game, "red");
        assertNotNull(player);
        assertEquals("testPlayer", player.getUsername());
        assertEquals(new Position(1, 1), player.getPlayerPosition());
        assertEquals("red", player.getPlayerColor());
    }

    @Test
    public void testUpdatePlayerPosition() {
        Player player = new Player("testPlayer", new Position(1, 1), game, "red");
        Map mockMap = Mockito.mock(Map.class);
        when(mockMap.getCellValue(anyInt(), anyInt())).thenReturn('.');
        when(mockMap.getMap()).thenReturn(testMapContent_charArr); // Add this line

        playerService.updatePlayerPosition(player, new Position(2, 2), mockMap, new ArrayList<>(), new ArrayList<>());
        assertEquals(new Position(2, 2), player.getPlayerPosition());
    }

    @Test
    public void testSetInitialRandomRole() {
        Player player = new Player("testPlayer", new Position(1, 1), game, "red");
        playerService.setInitialRandomRole(5, 2, player);
        assertNotNull(player.getRole());
    }

    @Test
    public void testSetRandomRole() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("testPlayer1", new Position(1, 1), game, "red"));
        players.add(new Player("testPlayer2", new Position(2, 2), game, "blue"));
        playerService.setRandomRole(players);
        for (Player player : players) {
            assertNotNull(player.getRole());
        }
    }

    @Test
    public void testCalculateNewPosition() {
        Player player = new Player("testPlayer", new Position(1, 1), game, "red");
        Position newPosition = playerService.calculateNewPosition(player.getPlayerPosition(), "KeyD", new ArrayList<>(), player);
        assertEquals(new Position(2, 1), newPosition);
    }

    @Test
    public void testUpdatePlayerMirrored() {
        Player player = new Player("testPlayer", new Position(1, 1), game, "red");
        playerService.updatePlayerMirrored(player, true);
        assertTrue(player.isMirrored());
    }

    @Test
    public void testUpdatePlayerisMoving() {
        Player player = new Player("testPlayer", new Position(1, 1), game, "red");
        playerService.updatePlayerisMoving(player, true);
        assertTrue(player.isMoving());
    }

    @Test
    public void testSendPlayerToVent() {
        Player player = new Player("testPlayer", new Position(1, 1), game, "red");
        List<Pair<Position>> ventPositions = Arrays.asList(
                new Pair<>(new Position(1, 1), new Position(2, 2)),
                new Pair<>(new Position(3, 3), new Position(4, 4))
        );
        Position newPosition = playerService.sendPlayerToVent(player, ventPositions);
        assertEquals(new Position(2, 2), newPosition);
    }
}