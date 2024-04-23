package at.fhv.tasks.model.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasscodeTaskMessage {
    private int value;

    public PasscodeTaskMessage() {
    }

    public PasscodeTaskMessage(int value) {
        this.value = value;
    }
}
