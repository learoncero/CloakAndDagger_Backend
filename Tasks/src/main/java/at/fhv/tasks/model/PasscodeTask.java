package at.fhv.tasks.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PasscodeTask {
    private int currentSum;
    private int randomSum;
    private boolean taskDone;

    public PasscodeTask() {
        this.currentSum = 0;
        this.randomSum = 0;
        this.taskDone = false;
    }
}
