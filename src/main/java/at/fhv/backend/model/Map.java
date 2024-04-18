package at.fhv.backend.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Map {
    private int id;
    private boolean[][] map;
    private String name;
    private static int nextId = 1;

    public Map() {
    }

    public Map(String name, boolean[][] map) {
        this.id = nextId++;
        this.map = map;
        this.name = name;
    }

    public boolean getCellValue(int x, int y) {
        return map[y][x];
    }
}
