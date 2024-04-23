package at.fhv.game;

import at.fhv.game.utils.MapLoader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTest {

    @Test
    public void testLoadMapFromFile() {
        // Prepare a temporary test file
        String testMapFileName = "testmap";
        String testMapContent = ".#..\n" +
                "..#.\n" +
                "....";
        String testMapFilePath = "src/main/java/at/fhv/backend/repository/" + testMapFileName + ".txt";
        createTestMapFile(testMapFilePath, testMapContent);

        // Load the map from the test file
        boolean[][] loadedMap = MapLoader.loadMapFromFile(testMapFileName);

        // Check if the loaded map is not null
        assertNotNull(loadedMap);

        // Check if the dimensions of the loaded map are correct
        assertEquals(3, loadedMap.length);
        assertEquals(4, loadedMap[0].length);

        // Check specific cells to ensure correct loading
        assertTrue(loadedMap[0][0]); // Should be walkable
        assertFalse(loadedMap[0][1]); // Should not be walkable
        assertTrue(loadedMap[0][2]); // Should not be walkable
        assertTrue(loadedMap[0][3]); // Should be walkable
        assertTrue(loadedMap[1][0]); // Should not be walkable
        assertTrue(loadedMap[1][1]); // Should not be walkable
        assertFalse(loadedMap[1][2]); // Should be walkable
        assertTrue(loadedMap[1][3]); // Should not be walkable
        assertTrue(loadedMap[2][0]); // Should not be walkable
        assertTrue(loadedMap[2][1]); // Should not be walkable
        assertTrue(loadedMap[2][2]); // Should not be walkable
        assertTrue(loadedMap[2][3]); // Should not be walkable

        // Clean up the test file
        deleteTestMapFile(testMapFilePath);
    }

    // Helper method to create a test map file with given content
    private void createTestMapFile(String filePath, String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
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
