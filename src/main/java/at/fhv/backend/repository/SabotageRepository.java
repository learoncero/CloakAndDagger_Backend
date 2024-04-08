package at.fhv.backend.repository;

import at.fhv.backend.model.Sabotage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
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
        sabotages.add(new Sabotage(1, "Cutting power cables", "Disables electricity supply to critical systems"));
        sabotages.add(new Sabotage(2, "Injecting malware into system", "Corrupts data and disrupts operations"));
        sabotages.add(new Sabotage(3, "Tampering with security cameras", "Creates blind spots in surveillance"));
        sabotages.add(new Sabotage(4, "Blocking network communication", "Prevents devices from connecting to each other"));
        sabotages.add(new Sabotage(5, "Sabotaging equipment", "Physically damaging machinery or tools"));
    }
}
