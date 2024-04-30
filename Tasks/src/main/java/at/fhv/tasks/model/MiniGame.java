package at.fhv.tasks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MiniGame {
    private int id;
    private String title;
    private String description;

    public MiniGame(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
