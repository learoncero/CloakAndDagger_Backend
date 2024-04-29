package at.fhv.tasks.service;

import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.repository.PasscodeTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PasscodeTaskService {
    private final PasscodeTaskRepository passcodeTaskRepository;

    @Autowired
    public PasscodeTaskService(PasscodeTaskRepository passcodeTaskRepository) {
        this.passcodeTaskRepository = passcodeTaskRepository;
    }

    public PasscodeMiniGame getInstance(String gameCode, int taskId) {
        return passcodeTaskRepository.getInstance(gameCode, taskId);
    }

    public void addToSum(int value, int taskId, String gameCode) {
        PasscodeMiniGame passcodeMiniGame = passcodeTaskRepository.getInstance(gameCode, taskId);
        if (passcodeMiniGame != null) {
            passcodeMiniGame.setCurrentSum(passcodeMiniGame.getCurrentSum() + value);
        }
    }

    public PasscodeMiniGame generateRandomSum(PasscodeMiniGame passcodeMiniGame) {
        if (passcodeMiniGame.getRandomSum() == 0) {
            passcodeMiniGame.setRandomSum(ThreadLocalRandom.current().nextInt(31, 83));
        }
        return passcodeMiniGame;
    }

    public void saveNewInstance(String gameCode, int taskId, PasscodeMiniGame miniGame) {
        passcodeTaskRepository.saveNewInstance(gameCode, taskId, miniGame);
    }

    public void deleteInstance(String gameCode, int taskId) {
        passcodeTaskRepository.removeInstance(gameCode, taskId);
    }
}
