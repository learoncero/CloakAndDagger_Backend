package at.fhv.game.repository;

import at.fhv.game.model.Sabotage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SabotageRepository {
    private final List<Sabotage> sabotages;

    public SabotageRepository() {
        this.sabotages = new ArrayList<>();
        initializeSabotages();
    }

    public List<Sabotage> getAllSabotages() {
        return sabotages;
    }

    private void initializeSabotages() {
        sabotages.add(new Sabotage(1, "Impaired Visuals", "Reduces visible area drastically"));
        sabotages.add(new Sabotage(2, "Mirror Madness", "Reverses movement controls"));
        sabotages.add(new Sabotage(3, "Navigation Jam", "Disables the minimap"));
        sabotages.add(new Sabotage(4, "Thug Barricade", "Challenge thugs to proceed."));
    }
}
