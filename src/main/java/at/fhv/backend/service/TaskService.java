package at.fhv.backend.service;

import at.fhv.backend.model.PasscodeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class TaskService {
    private final PasscodeTask passcodeTask;
    private Integer randomSum;

    @Autowired
    public TaskService(PasscodeTask passcodeTask) {
        this.passcodeTask = passcodeTask;
        this.randomSum = passcodeTask.getRandomSum();
    }

    public void addToSum(int value) {
        passcodeTask.setCurrentSum(passcodeTask.getCurrentSum() + value);
    }

    public Integer getCurrentSum() {
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
}
