package at.fhv.game.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

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

    @Schema(description = "List of vent positions (in pairs) for the map")
    private List<Pair<Position>> ventPositions;

    public Map() {
    }

    public Map(String name, char[][] map) {
        this.id = nextId++;
        this.map = map;
        this.name = name;
        this.ventPositions = getVentPositions(map);
    }

    public char getCellValue(int x, int y) {
        return map[y][x];
    }

    private List<Pair<Position>> getVentPositions(char[][] map) {
        List<Pair<Position>> ventPositions = new ArrayList<>();
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                if(Character.isDigit(map[i][j])) {
                    Position ventPos = new Position(j, i);
                    int charValue = Character.getNumericValue(map[i][j]);
                    while (ventPositions.size() <= charValue) {
                        ventPositions.add(new Pair<>(null, null));
                    }
                    Pair<Position> pair = ventPositions.get(charValue);
                    pair.add(ventPos);
                }
            }
        }
        return ventPositions;
    }
}
