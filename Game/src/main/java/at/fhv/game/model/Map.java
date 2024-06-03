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
    private List<List<Position>> ventPositions;

    public Map() {
    }

    public Map(String name, char[][] map) {
        this.id = nextId++;
        this.map = map;
        this.name = name;
        this.ventPositions = getVentPositions(map);
        System.out.println("ventPositions: ");
        for(List<Position> ventPos : ventPositions) {
            System.out.println(ventPos.get(0).getX() + ", " + ventPos.get(0).getY() +
                    " - " + ventPos.get(1).getX() + ", " + ventPos.get(1).getY());
        }
    }

    public char getCellValue(int x, int y) {
        return map[y][x];
    }

    public List<List<Position>> getVentPositions(char[][] map) {
        List<List<Position>> ventPositions = new ArrayList<>();
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                if(Character.isDigit(map[i][j])) {
                    Position ventPos = new Position(j, i);
                    int charValue = Character.getNumericValue(map[i][j]);
                    while (ventPositions.size() <= charValue) {
                        ventPositions.add(new ArrayList<>());
                    }
                    ventPositions.get(charValue).add(ventPos);
                }
            }
        }
        return ventPositions;
    }
}
