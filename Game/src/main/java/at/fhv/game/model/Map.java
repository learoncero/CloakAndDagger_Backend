package at.fhv.game.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Map {
    private static int nextId = 1;
    @Schema(description = "Unique identifier for the map")
    private int id;

    @Schema(description = "The map data represented as a 2D array of characters")
    private char[][] map;

    @Schema(description = "The name of the map")
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
