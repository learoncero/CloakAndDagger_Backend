package at.fhv.backend.service;

import at.fhv.backend.model.Game;
import at.fhv.backend.model.Sabotage;
import at.fhv.backend.repository.SabotageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SabotageService {
    private final SabotageRepository sabotageRepository;

    public SabotageService(SabotageRepository sabotageRepository) {
        this.sabotageRepository = sabotageRepository;
    }

    public List<Sabotage> getAllSabotages() {
        return sabotageRepository.getAllSabotages();
    }

    public void addSabotagesToGame(Game game) {
        game.setSabotages(getAllSabotages());
    }
}
