package at.fhv.tasks.model.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasscodeTaskMessage {
    private int value;
    private int taskId;

    public PasscodeTaskMessage() {
    }

    public PasscodeTaskMessage(int value, int taskId) {
        this.value = value;
        this.taskId = taskId;
    }
}
