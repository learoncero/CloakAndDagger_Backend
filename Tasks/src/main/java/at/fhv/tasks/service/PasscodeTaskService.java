package at.fhv.tasks.service;

import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.repository.PasscodeTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PasscodeTaskService {
    private final PasscodeTaskRepository passcodeTaskRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public PasscodeTaskService(PasscodeTaskRepository passcodeTaskRepository) {
        this.passcodeTaskRepository = passcodeTaskRepository;
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
        int random = (int) Math.floor(Math.random() * (85 - 32 + 1) + 32);
        return random;
    }

    public void saveNewInstance(String gameCode, int taskId, PasscodeMiniGame miniGame) {
        passcodeTaskRepository.saveInstance(gameCode, taskId, miniGame);
    }

    private void deleteInstance(String gameCode, int taskId) {
        passcodeTaskRepository.removeInstance(gameCode, taskId);
    }
}
