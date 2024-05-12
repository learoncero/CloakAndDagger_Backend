package at.fhv.minigames.service;

import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import at.fhv.minigames.repository.DecipherSymbolsMiniGameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DecipherSymbolsMiniGameServiceTests {

    @MockBean
    private DecipherSymbolsMiniGameRepository decipherSymbolsMiniGameRepository;

    @Autowired
    private DecipherSymbolsMiniGameService decipherSymbolsMiniGameService;

    @Test
    public void testGetInstance() {
        String gameCode = "TEST_GAME";
        int taskId = 1;
        DecipherSymbolsMiniGame miniGame = new DecipherSymbolsMiniGame(3, "Test Game", "This is a test game");

        when(decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        DecipherSymbolsMiniGame retrievedGame = decipherSymbolsMiniGameService.getInstance(gameCode, taskId);

        assertNotNull(retrievedGame);
        assertEquals(miniGame, retrievedGame);
    }

    @Test
    public void testCreateShuffledSymbols() {
        List<String> symbols = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25");
        int currentRound = 1;

        List<String> shuffledSymbols = decipherSymbolsMiniGameService.createShuffledSymbols(symbols, currentRound);

        assertEquals(shuffledSymbols.size(), 9);
    }

    @Test
    public void testGetCorrectSymbol() {
        List<String> symbols = Arrays.asList("1", "2", "3");

        String correctSymbol = decipherSymbolsMiniGameService.getCorrectSymbol(symbols);

        assertNotNull(correctSymbol);
        assertTrue(symbols.contains(correctSymbol));
    }

    @Test
    public void testVerifySymbolCorrect() {
        String gameCode = "TEST_GAME";
        int taskId = 1;
        int currentRound = 1;
        String symbol = "1";
        DecipherSymbolsMiniGame miniGame = new DecipherSymbolsMiniGame(3, "Test Game", "This is a test game");
        miniGame.setCorrectSymbol(symbol);
        miniGame.setShuffledSymbols(Arrays.asList("1", "2", "3"));

        when(decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        boolean isSymbolCorrect = decipherSymbolsMiniGameService.verifySymbol(symbol, taskId, gameCode, currentRound);

        assertTrue(isSymbolCorrect);
    }

    @Test
    public void testVerifySymbolIncorrect() {
        String gameCode = "TEST_GAME";
        int taskId = 1;
        int currentRound = 1;
        String symbol = "2";
        DecipherSymbolsMiniGame miniGame = new DecipherSymbolsMiniGame(3, "Test Game", "This is a test game");
        miniGame.setCorrectSymbol("1");
        miniGame.setShuffledSymbols(Arrays.asList("1", "2", "3"));

        when(decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        boolean isSymbolCorrect = decipherSymbolsMiniGameService.verifySymbol(symbol, taskId, gameCode, currentRound);

        assertFalse(isSymbolCorrect);
    }

    @Test
    public void testDeleteInstanceNotFinished() {
        String gameCode = "TEST_GAME";
        int taskId = 1;
        int currentRound = 2;
        String symbol = "1";
        DecipherSymbolsMiniGame miniGame = new DecipherSymbolsMiniGame(3, "Test Game", "This is a test game");
        miniGame.setCorrectSymbol(symbol);
        miniGame.setShuffledSymbols(Arrays.asList("1", "2", "3"));

        when(decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId)).thenReturn(miniGame);

        decipherSymbolsMiniGameService.verifySymbol(symbol, taskId, gameCode, currentRound);

        Map<Integer, DecipherSymbolsMiniGame> games = decipherSymbolsMiniGameRepository.getTaskMap().get(gameCode);
        assertNull(games);
    }
}