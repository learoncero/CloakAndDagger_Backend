package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.Sabotage;
import at.fhv.game.repository.SabotageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SabotageService {
    private final SabotageRepository sabotageRepository;

    @Autowired
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
