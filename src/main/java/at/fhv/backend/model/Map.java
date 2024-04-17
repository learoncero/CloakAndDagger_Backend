package at.fhv.backend.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Map {
    private boolean[][] map;
    private String name;

    public Map() {
    }

    public Map(boolean[][] map, String name) {
        this.map = map;
        this.name = name;
    }
}
