package at.fhv.game.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapLoaderTest {

    @Test
    public void testLoadMapFromFile() {
        // Prepare a temporary test file
        String testMapFileName = "testmap";
        String testMapContent =
                """
                        .#..
                        ..#.
                        ..T.
                        """;
        String testMapFilePath = "Game/src/main/java/at/fhv/game/repository/" + testMapFileName + ".txt";
        createTestMapFile(testMapFilePath, testMapContent);

        // Load the map from the test file
        char[][] loadedMap = MapLoader.loadMapFromFile(testMapFileName);

        // Check if the loaded map is not null
        assertNotNull(loadedMap);

        // Check if the dimensions of the loaded map are correct
        assertEquals(3, loadedMap.length);
        assertEquals(4, loadedMap[0].length);

        // Check specific cells to ensure correct loading
        assertEquals('.', loadedMap[0][0]); // Should be walkable
        assertEquals('#', loadedMap[0][1]); // Should not be walkable
        assertEquals('.', loadedMap[0][2]); // Should not be walkable
        assertEquals('.', loadedMap[0][3]); // Should be walkable
        assertEquals('.', loadedMap[1][0]); // Should not be walkable
        assertEquals('.', loadedMap[1][1]); // Should not be walkable
        assertEquals('#', loadedMap[1][2]); // Should be walkable
        assertEquals('.', loadedMap[1][3]); // Should not be walkable
        assertEquals('.', loadedMap[2][0]); // Should not be walkable
        assertEquals('.', loadedMap[2][1]); // Should not be walkable
        assertEquals('T', loadedMap[2][2]); // Should not be walkable
        assertEquals('.', loadedMap[2][3]); // Should not be walkable

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
