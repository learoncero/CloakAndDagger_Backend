package at.fhv.game.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Scanner;

@Component
public class MapLoader {

    public static char[][] loadMapFromFile(String map) {
        char[][] mapCells = null;
        String resourcePath = "at/fhv/game/repository/" + map.toLowerCase() + ".txt";

        try (InputStream inputStream = MapLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + resourcePath);
            }

            try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(inputStream)))) {
                int rows = 0;
                int cols = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    rows++;
                    cols = Math.max(cols, line.length());
                }
                mapCells = new char[rows][cols];

                try (InputStream newInputStream = MapLoader.class.getClassLoader().getResourceAsStream(resourcePath);
                     Scanner mapScanner = new Scanner(new BufferedReader(new InputStreamReader(newInputStream)))) {
                    int row = 0;
                    while (mapScanner.hasNextLine()) {
                        String line = mapScanner.nextLine();
                        for (int col = 0; col < line.length(); col++) {
                            mapCells[row][col] = line.charAt(col);
                        }
                        row++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapCells;
    }
}
