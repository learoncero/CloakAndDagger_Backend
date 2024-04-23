package at.fhv.game;

import at.fhv.game.utils.RandomRoleAssigner;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RandomRoleAssignerTest {

    @Test
    public void testAssignRandomRoles() {
        int numPlayers = 10;
        int numImpostors = 3;

        List<Integer> impostors = RandomRoleAssigner.assignRandomRoles(numPlayers, numImpostors);

        // Check if impostors list is not null
        assertNotNull(impostors);

        // Check if the number of impostors assigned matches the expected number
        assertEquals(numImpostors, impostors.size());

        // Check if all impostor indices are within the valid range of player indices
        for (int impostorIndex : impostors) {
            assertTrue(impostorIndex >= 0 && impostorIndex < numPlayers);
        }

        // Check if impostor indices are unique
        for (int i = 0; i < impostors.size(); i++) {
            for (int j = i + 1; j < impostors.size(); j++) {
                assertNotEquals(impostors.get(i), impostors.get(j));
            }
        }
    }

    @Test
    public void testGetImpostorsIndices() {
        // Assign some random roles first
        RandomRoleAssigner.assignRandomRoles(8, 2);

        List<Integer> impostors = RandomRoleAssigner.getImpostorsIndices();

        // Check if impostors list is not null
        assertNotNull(impostors);

        // Check if the size of impostors list matches the expected number of impostors
        assertEquals(2, impostors.size());

        // Check if the indices in the impostors list are within the valid range of player indices
        for (int impostorIndex : impostors) {
            assertTrue(impostorIndex >= 0 && impostorIndex < 8);
        }
    }
}
