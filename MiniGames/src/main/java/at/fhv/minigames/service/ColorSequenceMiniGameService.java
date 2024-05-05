package at.fhv.minigames.service;

import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.repository.ColorSeqMiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ColorSequenceMiniGameService {
    private final at.fhv.minigames.repository.ColorSeqMiniGameRepository colorSeqMiniGameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ColorSequenceMiniGameService(ColorSeqMiniGameRepository colorSeqMiniGameRepository) {
        this.colorSeqMiniGameRepository = colorSeqMiniGameRepository;
    }

    public ColorSeqMiniGame getInstance(String gameCode, int taskId) {
        return colorSeqMiniGameRepository.getInstance(gameCode, taskId);
    }

    public List<String> createShuffledSequence(List<String> colors) {
        Collections.shuffle(colors);
        return colors;
    }

    public boolean verifySequence(List<String> submittedColors, List<String> shuffledColors, int taskId, String gameCode) {
        ColorSeqMiniGame colorSeqMiniGame = colorSeqMiniGameRepository.getInstance(gameCode, taskId);
        if(colorSeqMiniGame != null) {
            colorSeqMiniGame.setEqual(submittedColors.equals(shuffledColors));
            if(colorSeqMiniGame.isEqual()) {
                restTemplate.postForEntity("http://localhost:5010/api/game/task/" + gameCode + "/done", taskId, Void.class);
                deleteInstance(gameCode, taskId);
            } else {
                colorSeqMiniGameRepository.saveInstance(gameCode,taskId,colorSeqMiniGame);
            }

        }

        assert colorSeqMiniGame != null;
        return colorSeqMiniGame.isEqual();
    }
    public void saveNewInstance(String gameCode, int taskId, ColorSeqMiniGame miniGame) {
        colorSeqMiniGameRepository.saveInstance(gameCode, taskId, miniGame);
    }

    private void deleteInstance(String gameCode, int taskId) {
        colorSeqMiniGameRepository.removeInstance(gameCode, taskId);
    }
}
