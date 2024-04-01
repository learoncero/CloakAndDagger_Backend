package at.fhv.backend.model;

public class Map {
    private boolean[][] map;

    public Map() {
    }

    public boolean[][] getMap() {
        return map;
    }

    public void setMap(boolean[][] mapArray) {
        this.map = mapArray;
    }

    public void setMapbyPosition(int x, int y, boolean value) {
        map[x][y] = value;
    }

    public void setInitialMap() {
        if (map == null) {
            map = MapLoader.loadMapFromFile();
        }
    }

    public boolean getCellValue(int x, int y) {
        return map[y][x];
    }

}
