package at.fhv.game.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomRoleAssigner {

    public static List<Integer> assignRandomRoles(int numPlayers, int numImpostors) {
        Random random = new Random();
        List<Integer> impostors = new ArrayList<>();
        //fill impostors list with random Indices for players to be impostors
        while (impostors.size() < numImpostors) {
            int randomIndex = random.nextInt(numPlayers);
            if (!impostors.contains(randomIndex)) {
                impostors.add(randomIndex);
            }
        }
        return impostors;
    }
}