package at.fhv.tasks.service;

import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.repository.MiniGameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MiniGameServiceTest {

    @Mock
    private MiniGameRepository miniGameRepository;

    @InjectMocks
    private MiniGameService miniGameService;

    @Test
    public void testGetAllMiniGames() {
        List<MiniGame> expectedMiniGames = Arrays.asList(
                new MiniGame(1, "Test Game 1", "Description 1"),
                new MiniGame(2, "Test Game 2", "Description 2")
        );
        Mockito.when(miniGameRepository.getAllMiniGames()).thenReturn(expectedMiniGames);

        List<MiniGame> actualMiniGames = miniGameService.getAllMiniGames();

        assertThat(actualMiniGames).isEqualTo(expectedMiniGames);
    }
}

