package at.fhv.game.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameCodeGeneratorTest {

    @Test
    public void testGenerateGameCode() {
        final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int EXPECTED_CODE_LENGTH = 6;

        String code = GameCodeGenerator.generateGameCode();

        // Check if code is not null
        assertNotNull(code);

        // Check if code length is correct
        assertEquals(EXPECTED_CODE_LENGTH, code.length());

        // Check if code contains only valid characters
        for (char c : code.toCharArray()) {
            assertTrue(VALID_CHARACTERS.contains(String.valueOf(c)));
        }

        // Check if generated codes are unique
        String anotherCode = GameCodeGenerator.generateGameCode();
        assertNotEquals(code, anotherCode);
    }
}
