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

    //TODO: Implement logic that it doesn't get the first task, but the next one that is not done

    public void addToSum(int value, String gameCode) {
        PasscodeMiniGame task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        if (task != null) {
            task.setCurrentSum(task.getCurrentSum() + value);
        }
    }

    public int getCurrentSum(String gameCode) {
        PasscodeMiniGame task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        return task != null ? task.getCurrentSum() : 0;
    }

    public int generateRandomSum(String gameCode) {
        PasscodeMiniGame task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        if (task != null && task.getRandomSum() == 0) {
            task.setRandomSum(ThreadLocalRandom.current().nextInt(1, 51));
        }
        return task != null ? task.getRandomSum() : 0;
    }

    public void resetSum(String gameCode) {
        PasscodeMiniGame task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        if (task != null) {
            task.setCurrentSum(0);
        }
    }

    public int getRandomSum(String gameCode) {
        PasscodeMiniGame task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        return task != null ? task.getRandomSum() : 0;
    }

    public void createNewInstance(String gameCode, PasscodeMiniGame miniGame) {
        passcodeTaskRepository.createNewInstance(gameCode, miniGame);
    }
}
