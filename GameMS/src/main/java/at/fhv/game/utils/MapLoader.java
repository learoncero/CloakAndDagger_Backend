package at.fhv.game.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
public class MapLoader {
    private static final String pathForMap = "GameMS/src/main/java/at/fhv/backend/repository/";

    public static boolean[][] loadMapFromFile(String map) {
        boolean[][] walkableCells = null;
        String newPathForMap = pathForMap + map.toLowerCase() + ".txt";
        try (Scanner scanner = new Scanner(new File(newPathForMap))) {
            int rows = 0;
            int cols = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                rows++;
                cols = Math.max(cols, line.length());
                //System.out.println("Rows: "+rows+" Cols: "+cols);
            }
            walkableCells = new boolean[rows][cols];
            try (Scanner booleanchecker = new Scanner(new File(newPathForMap))) {
                int row = 0;
                while (booleanchecker.hasNextLine()) {
                    String line = booleanchecker.nextLine();
                    //System.out.println("Line: "+line);
                    for (int col = 0; col < line.length(); col++) {
                        walkableCells[row][col] = line.charAt(col) == '.';
                    }
                    row++;
                }
                //System.out.println(Arrays.deepToString(walkableCells));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return walkableCells;
    }//TODO printStackTrace causes warning: probably should be replaced with more robust logging
}
