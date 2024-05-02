package at.fhv.tasks.service;

import at.fhv.tasks.model.ColorSeqMiniGame;
import at.fhv.tasks.repository.ColorSeqMiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ColorSequenceMiniGameService {
    private final ColorSeqMiniGameRepository ColorSeqMiniGameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ColorSequenceMiniGameService(ColorSeqMiniGameRepository colorSeqMiniGameRepository) {
        this.ColorSeqMiniGameRepository = colorSeqMiniGameRepository;
    }

    public ColorSeqMiniGame getInstance(String gameCode, int taskId) {
        return ColorSeqMiniGameRepository.getInstance(gameCode, taskId);
    }

    public List<String> createShuffledSequence(List<String> colors) {
        Collections.shuffle(colors);
        return colors;
    }

    public boolean verifySequence(List<String> submittedColors, List<String> shuffledColors,int taskId, String gameCode) {
        ColorSeqMiniGame colorSeqMiniGame = ColorSeqMiniGameRepository.getInstance(gameCode, taskId);
        if(colorSeqMiniGame != null) {
            colorSeqMiniGame.setEqual(submittedColors.equals(shuffledColors));
            if(colorSeqMiniGame.isEqual() == true) {
                restTemplate.postForEntity("http://localhost:5010/api/game/task/" + gameCode + "/done", taskId, Void.class);
                deleteInstance(gameCode, taskId);
            } else {
                ColorSeqMiniGameRepository.saveInstance(gameCode,taskId,colorSeqMiniGame);
            }

        }
        return colorSeqMiniGame.isEqual();
    }
    public void saveNewInstance(String gameCode, int taskId, ColorSeqMiniGame miniGame) {
        ColorSeqMiniGameRepository.saveInstance(gameCode, taskId, miniGame);
    }

    private void deleteInstance(String gameCode, int taskId) {
        ColorSeqMiniGameRepository.removeInstance(gameCode, taskId);
    }
}
