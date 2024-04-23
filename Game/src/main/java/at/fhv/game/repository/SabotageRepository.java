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
        sabotages.add(new Sabotage(1, "Cutting power cables", "Disables electricity supply to critical systems",0));
        sabotages.add(new Sabotage(2, "Injecting malware into system", "Corrupts data and disrupts operations",1));
        sabotages.add(new Sabotage(3, "Tampering with security cameras", "Creates blind spots in surveillance",2));
        sabotages.add(new Sabotage(4, "Blocking network communication", "Prevents devices from connecting to each other", 1));
        sabotages.add(new Sabotage(5, "Sabotaging equipment", "Physically damaging machinery or tools", 2));
    }
}
