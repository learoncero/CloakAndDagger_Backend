package at.fhv.minigames.service;

import at.fhv.minigames.model.PasscodeMiniGame;
import at.fhv.minigames.repository.PasscodeMiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PasscodeMiniGameService {
    private final PasscodeMiniGameRepository passcodeTaskRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public PasscodeMiniGameService(PasscodeMiniGameRepository passcodeMiniGameRepository) {
        this.passcodeTaskRepository = passcodeMiniGameRepository;
    }

    public PasscodeMiniGame getInstance(String gameCode, int taskId) {
        return passcodeTaskRepository.getInstance(gameCode, taskId);
    }

    public int addToSum(int value, int taskId, String gameCode) {
        PasscodeMiniGame passcodeMiniGame = passcodeTaskRepository.getInstance(gameCode, taskId);

        if (passcodeMiniGame != null) {
            passcodeMiniGame.setCurrentSum(passcodeMiniGame.getCurrentSum() + value);
            if (passcodeMiniGame.getCurrentSum() == passcodeMiniGame.getRandomSum()) {
                int currentSum = passcodeMiniGame.getCurrentSum();
                restTemplate.postForEntity("http://localhost:5010/api/game/task/" + gameCode + "/done", taskId, Void.class);
                deleteInstance(gameCode, taskId);
                return currentSum;
            } else if (passcodeMiniGame.getCurrentSum() > passcodeMiniGame.getRandomSum()) {
                int currentSum = passcodeMiniGame.getCurrentSum();
                passcodeMiniGame.setCurrentSum(0);
                passcodeTaskRepository.saveInstance(gameCode, taskId, passcodeMiniGame);
                return currentSum;
            } else {
                passcodeTaskRepository.saveInstance(gameCode, taskId, passcodeMiniGame);
                return passcodeMiniGame.getCurrentSum();
            }
        }
        return -1;
    }

    public int generateRandomSum() {
        return (int) Math.floor(Math.random() * (85 - 32 + 1) + 32);
    }

    public void saveNewInstance(String gameCode, int taskId, PasscodeMiniGame miniGame) {
        passcodeTaskRepository.saveInstance(gameCode, taskId, miniGame);
    }

    public void deleteInstance(String gameCode, int taskId) {
        passcodeTaskRepository.removeInstance(gameCode, taskId);
    }
}
