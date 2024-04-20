package at.fhv.backend.service;

import at.fhv.backend.model.PasscodeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final PasscodeTask passcodeTask;

    @Autowired
    public TaskService(PasscodeTask passcodeTask) {
        this.passcodeTask = passcodeTask;
    }

    public void addToSum(int value) {
        passcodeTask.setCurrentSum(passcodeTask.getCurrentSum() + value);
        System.out.println("Current sum: " + passcodeTask.getCurrentSum());
    }

    public Integer getCurrentSum() {
        return passcodeTask.getCurrentSum();
    }
}
