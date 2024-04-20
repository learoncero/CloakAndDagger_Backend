package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PasscodeTask {
    private int currentSum;
    private int randomSum;

    public PasscodeTask() {
        this.currentSum = 0;
        this.randomSum = 0;
    }
}
