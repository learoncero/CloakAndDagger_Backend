package at.fhv.tasks.service;

import at.fhv.tasks.model.PasscodeTask;
import at.fhv.tasks.repository.PasscodeTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PasscodeTaskService {
    private final PasscodeTask passcodeTask;
    private Integer randomSum;
    private final PasscodeTaskRepository passcodeTaskRepository;

    @Autowired
    public PasscodeTaskService(PasscodeTask passcodeTask, PasscodeTaskRepository passcodeTaskRepository) {
        this.passcodeTask = passcodeTask;
        this.randomSum = passcodeTask.getRandomSum();
        this.passcodeTaskRepository = passcodeTaskRepository;
    }

    //TODO: Implement logic that it doesn't get the first task, but the next one that is not done

    public void createTasksForGame(String gameCode) {
        List<PasscodeTask> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(new PasscodeTask());
        }
        passcodeTaskRepository.saveTasksForGame(gameCode, tasks);
    }

    public void addToSum(int value, String gameCode) {
        PasscodeTask task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        if (task != null) {
            task.setCurrentSum(task.getCurrentSum() + value);
        }
    }

    public int getCurrentSum(String gameCode) {
        PasscodeTask task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        return task != null ? task.getCurrentSum() : 0;
    }

    public int generateRandomSum(String gameCode) {
        PasscodeTask task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        if (task != null && task.getRandomSum() == 0) {
            task.setRandomSum(ThreadLocalRandom.current().nextInt(1, 51));
        }
        return task != null ? task.getRandomSum() : 0;
    }

    public void resetSum(String gameCode) {
        PasscodeTask task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        if (task != null) {
            task.setCurrentSum(0);
        }
    }

    public int getRandomSum(String gameCode) {
        PasscodeTask task = passcodeTaskRepository.getFirstTaskForGame(gameCode);
        return task != null ? task.getRandomSum() : 0;
    }

    public void setTaskDone(boolean taskDone) {
        passcodeTask.setTaskDone(taskDone);
    }
}
