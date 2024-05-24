package at.fhv.minigames.repository;

import at.fhv.minigames.model.*;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class MiniGameRepository {
    private final List<MiniGame> miniGames;

    public MiniGameRepository() {
        this.miniGames = new ArrayList<>();
        initialiseMiniGames();
    }

    public List<MiniGame> getAllMiniGames() {
        return miniGames;
    }

    private void initialiseMiniGames() {
        miniGames.add(new PasscodeMiniGame(1, "Passcode", "Sum up numbers to get the right passcode"));
        miniGames.add(new ColorSeqMiniGame(2, "Color Sequence", "Choose the right order of Colors"));
        miniGames.add(new DecipherSymbolsMiniGame(3, "Decipher Symbols", "Decipher the symbols three times in a row"));
        miniGames.add(new SortingAlgorithmMiniGame(4, "Sorting Algorithm", "Sort the boxes in the right order"));
        miniGames.add(new MovingSquareMiniGame(5, "Moving Boxes", "Catch the moving box with the blue button"));
    }

    public MiniGame getMiniGameById(int id) {
        return miniGames.stream().filter(miniGame -> miniGame.getId() == id).findFirst().orElse(null);
    }
}
