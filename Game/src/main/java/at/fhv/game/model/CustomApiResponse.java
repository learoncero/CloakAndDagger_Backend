package at.fhv.game.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public CustomApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
