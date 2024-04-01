package at.fhv.backend.model;

import java.security.SecureRandom;

public class GameCodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6; // Length of the game code

    public static String generateGameCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }

    public static void main(String[] args) {
        // Example usage
        String gameCode = generateGameCode();
        System.out.println("Generated Game Code: " + gameCode);
    }
}
