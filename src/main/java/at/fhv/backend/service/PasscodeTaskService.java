package at.fhv.backend.service;

import at.fhv.backend.model.PasscodeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PasscodeTaskService {
    private final PasscodeTask passcodeTask;
    private Integer randomSum;

    @Autowired
    public PasscodeTaskService(PasscodeTask passcodeTask) {
        this.passcodeTask = passcodeTask;
        this.randomSum = passcodeTask.getRandomSum();
    }

    public void addToSum(int value) {
        passcodeTask.setCurrentSum(passcodeTask.getCurrentSum() + value);
    }

    public int getCurrentSum() {
        return passcodeTask.getCurrentSum();
    }

    public int generateRandomSum() {
        if (randomSum == 0) {
            randomSum = ThreadLocalRandom.current().nextInt(1, 51);
        }
        return randomSum;
    }

    public void resetSum() {
        passcodeTask.setCurrentSum(0);
    }

    public int getRandomSum() {
        return randomSum;
    }

    public void setTaskDone(boolean taskDone) {
        passcodeTask.setTaskDone(taskDone);
    }

    public boolean isTaskDone() {
        return passcodeTask.isTaskDone();
    }
}
