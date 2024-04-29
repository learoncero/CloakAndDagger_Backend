package at.fhv.tasks.repository;

import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.model.PasscodeMiniGame;
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
    }

    public MiniGame getMiniGameById(int id) {
        return miniGames.stream().filter(miniGame -> miniGame.getId() == id).findFirst().orElse(null);
    }
}
