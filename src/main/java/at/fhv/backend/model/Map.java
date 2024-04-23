package at.fhv.backend.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Map {
    private static int nextId = 1;
    private int id;
    private char[][] map;
    private String name;

    public Map() {
    }

    public Map(String name, char[][] map) {
        this.id = nextId++;
        this.map = map;
        this.name = name;
    }

    public char getCellValue(int x, int y) {
        return map[y][x];
    }
}
