package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.Position;
import at.fhv.game.model.Sabotage;
import at.fhv.game.repository.SabotageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SabotageService {
    private final SabotageRepository sabotageRepository;

    @Autowired
    public SabotageService(SabotageRepository sabotageRepository) {
        this.sabotageRepository = sabotageRepository;
    }

    public List<Sabotage> getAllSabotages() {
        List<Sabotage> sabotages = sabotageRepository.getAllSabotages();
        List<Sabotage> copiedList = new ArrayList<>();
        for (Sabotage s : sabotages) {
            copiedList.add(new Sabotage(s.getId(), s.getTitle(), s.getDescription()));
        }
        return copiedList;
    }

    public void addSabotagesToGame(Game game) {
        game.setSabotages(getAllSabotages());
    }
}
