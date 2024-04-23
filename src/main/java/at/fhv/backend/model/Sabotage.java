package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sabotage {
    private int id;
    private String title;
    private String description;
    private int miniGameId;

    public Sabotage(int id, String title, String description, int miniGameId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.miniGameId = miniGameId;
    }

    public Sabotage() {

    }
}
