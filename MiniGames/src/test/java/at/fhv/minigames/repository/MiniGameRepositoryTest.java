package at.fhv.minigames.repository;

import at.fhv.minigames.model.MiniGame;
import at.fhv.minigames.model.PasscodeMiniGame;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MiniGameRepositoryTest {

    private MiniGameRepository miniGameRepository;

    @Before
    public void setUp() {
        miniGameRepository = new MiniGameRepository();
    }

    @Test
    public void testGetAllMiniGames_DefaultGames() {
        List<MiniGame> actualMiniGames = miniGameRepository.getAllMiniGames();

        assertThat(actualMiniGames).hasSize(1);

        assertThat(actualMiniGames.get(0)).isInstanceOf(PasscodeMiniGame.class);
    }

    @Test
    public void testGetMiniGameById_ExistingGame() {
        MiniGame expectedGame = new PasscodeMiniGame(1, "Passcode", "Sum up numbers to get the right passcode");

        MiniGame retrievedGame = miniGameRepository.getMiniGameById(1);

        assertThat(retrievedGame.getId()).isEqualTo(expectedGame.getId());
    }

    @Test
    public void testGetMiniGameById_NotFound() {
        MiniGame retrievedGame = miniGameRepository.getMiniGameById(10);

        assertThat(retrievedGame).isNull();
    }
}



