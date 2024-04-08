package at.fhv.backend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomRoleAssigner {
    public static List<Integer> impostors = new ArrayList<>();

    public static List<Integer> assignRandomRoles(int numPlayers, int numImpostors) {
        impostors = new ArrayList<>();
        Random random = new Random();

        //fill impostors list with random Indices for players to be impostors
        while (impostors.size() < numImpostors) {
            int randomIndex = random.nextInt(numPlayers);
            if (!impostors.contains(randomIndex)) {
                impostors.add(randomIndex);
            }
        }
        return impostors;
    }

    public static List<Integer> getImpostorsIndices() {
        return impostors;
    }

}
