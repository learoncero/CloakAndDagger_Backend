package at.fhv.minigames.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import at.fhv.minigames.model.SortingAlgorithmMiniGame;
import at.fhv.minigames.repository.SortingAlgorithmMiniGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class SortingAlgorithmMiniGameServiceTest {

    @Mock
    private SortingAlgorithmMiniGameRepository repository;

    @InjectMocks
    private SortingAlgorithmMiniGameService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetInstance() {
        String gameCode = "game1";
        int taskId = 1;
        SortingAlgorithmMiniGame miniGame = new SortingAlgorithmMiniGame();

        when(repository.getInstance(gameCode, taskId)).thenReturn(miniGame);
        SortingAlgorithmMiniGame result = service.getInstance(gameCode, taskId);

        assertNotNull(result);
        assertEquals(miniGame, result);
    }

    @Test
    public void testShuffleArray() {
        List<Integer> array = List.of(1, 2, 3, 4, 5);
        List<Integer> shuffledArray = service.createShuffledBoxes(array);

        assertNotNull(shuffledArray);
        assertEquals(array.size(), shuffledArray.size());
        assertFalse(array.equals(shuffledArray)); // The array should be shuffled, hence not equal to the original.
    }

    @Test
    public void testIsSorted() {
        List<Integer> sortedList = List.of(1, 2, 3, 4, 5);
        List<Integer> unsortedList = List.of(5, 3, 1, 4, 2);

        assertTrue(service.isSorted(sortedList));
        assertFalse(service.isSorted(unsortedList));
    }
}
