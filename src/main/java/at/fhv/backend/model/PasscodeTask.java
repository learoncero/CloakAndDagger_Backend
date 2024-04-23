package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PasscodeTask extends Task {
    private int currentSum;
    private int randomSum;
    private boolean taskDone;

    public PasscodeTask() {
        this.currentSum = 0;
        this.randomSum = 0;
        this.taskDone = false;
    }
}
