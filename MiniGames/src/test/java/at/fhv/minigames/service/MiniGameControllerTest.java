package at.fhv.minigames.service;

import at.fhv.minigames.controller.MiniGameController;
import at.fhv.minigames.model.MiniGame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MiniGameControllerTest {

    @Mock
    private MiniGameService miniGameService;

    @InjectMocks
    private MiniGameController miniGameController;

    @Test
    public void testGetAllMiniGames() {
        List<MiniGame> expectedMiniGames = Arrays.asList(
                new MiniGame(1, "Test Game 1", "Description 1"),
                new MiniGame(2, "Test Game 2", "Description 2")
        );
        Mockito.when(miniGameService.getAllMiniGames()).thenReturn(expectedMiniGames);

        ResponseEntity<List<MiniGame>> response = miniGameController.getAllMiniGames();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedMiniGames);
    }
}

