package at.fhv.minigames.service;

import at.fhv.minigames.model.MovingSquareMiniGame;
import at.fhv.minigames.repository.MovingSquareMiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovingSquareMiniGameService {
    private final MovingSquareMiniGameRepository movingSquareMiniGameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public MovingSquareMiniGameService(MovingSquareMiniGameRepository movingSquareMiniGameRepository) {
        this.movingSquareMiniGameRepository = movingSquareMiniGameRepository;
    }

    public MovingSquareMiniGame getInstance(String gameCode, int taskId) {
        return movingSquareMiniGameRepository.getInstance(gameCode, taskId);
    }

    public boolean markAsCompleted(String gameCode, int taskId) {
        MovingSquareMiniGame movingSquareMiniGame = movingSquareMiniGameRepository.getInstance(gameCode, taskId);

        if (movingSquareMiniGame != null) {
            restTemplate.postForEntity("http://10.0.40.169:5010/api/game/task/" + gameCode + "/done", taskId, Void.class);
            deleteInstance(gameCode, taskId);
            return true;
        }

        return false;
    }

    public void deleteInstance(String gameCode, int taskId) {
        movingSquareMiniGameRepository.removeInstance(gameCode, taskId);
    }

    public void saveInstance(String gameCode, int taskId, MovingSquareMiniGame miniGame) {
        movingSquareMiniGameRepository.saveInstance(gameCode, taskId, miniGame);
    }
}
