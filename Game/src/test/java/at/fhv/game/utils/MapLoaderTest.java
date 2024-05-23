package at.fhv.game.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTest {

    @Test
    public void testLoadMapFromFile() {
        String testMapFileName = "testmap";
        String testMapContent =
                """
                .#..
                ..#.
                ..T.
                """;
        char[][] testMapContent_charArr = {
                {'.', '#', '.', '.'},
                {'.', '.', '#', '.'},
                {'.', '.', 'T', '.'}
        };

        createTestMapFile(testMapFileName, testMapContent);

        // Load the map from the test file
        char[][] loadedMap = MapLoader.loadMapFromFile(testMapFileName);

        // Check if the loaded map is not null
        assertNotNull(loadedMap);

        // Check if the loaded map matches the test map
        assertArrayEquals(testMapContent_charArr, loadedMap);

        // Clean up the test file
        deleteTestMapFile(testMapFileName);
    }

    // Helper method to create a test map file with given content
    private void createTestMapFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(MapLoader.pathForMap + fileName + ".txt");
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error creating FileWriter:");
            e.printStackTrace();
        }
    }

    // Helper method to delete the test map file
    private void deleteTestMapFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
