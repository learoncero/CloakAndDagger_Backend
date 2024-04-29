package at.fhv.tasks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MiniGame {
    private int id;
    private String name;
    private String description;

    public MiniGame(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
