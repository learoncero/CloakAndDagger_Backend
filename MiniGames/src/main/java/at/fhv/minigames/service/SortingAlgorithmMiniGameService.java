package at.fhv.minigames.service;

import at.fhv.minigames.model.SortingAlgorithmMiniGame;
import at.fhv.minigames.repository.SortingAlgorithmMiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SortingAlgorithmMiniGameService {
    private final SortingAlgorithmMiniGameRepository sortingAlgorithmMiniGameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public SortingAlgorithmMiniGameService(SortingAlgorithmMiniGameRepository sortingAlgorithmMiniGameRepository) {
        this.sortingAlgorithmMiniGameRepository = sortingAlgorithmMiniGameRepository;
    }

    public SortingAlgorithmMiniGame getInstance(String gameCode, int taskId) {
        return sortingAlgorithmMiniGameRepository.getInstance(gameCode, taskId);
    }

    public List<Integer> createShuffledBoxes(List<Integer> boxes) {
        List<Integer> shuffledBoxes = new ArrayList<>(boxes);

        do {
            Collections.shuffle(shuffledBoxes);
        } while (isSorted(shuffledBoxes));

        return shuffledBoxes;
    }

    public boolean isSorted(List<Integer> boxes) {
        for (int i = 1; i < boxes.size(); i++) {
            if (boxes.get(i - 1) > boxes.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean verifySorting(List<Integer> boxes, int taskId, String gameCode) {
        SortingAlgorithmMiniGame sortingAlgorithmMiniGame = sortingAlgorithmMiniGameRepository.getInstance(gameCode, taskId);
        if (sortingAlgorithmMiniGame != null) {
            boolean isSorted = isSorted(boxes);

            if (isSorted) {
                // Mark task as completed and notify game
                restTemplate.postForEntity("http://localhost:5010/api/game/task/" + gameCode + "/done", taskId, Void.class);
                deleteInstance(gameCode, taskId);
            }

            return isSorted;
        }

        return false;
    }

    public void saveNewInstance(String gameCode, int taskId, SortingAlgorithmMiniGame miniGame) {
        sortingAlgorithmMiniGameRepository.saveInstance(gameCode, taskId, miniGame);
    }

    public void deleteInstance(String gameCode, int taskId) {
        sortingAlgorithmMiniGameRepository.removeInstance(gameCode, taskId);
    }
}

