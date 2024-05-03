package at.fhv.game.service;

import at.fhv.game.model.Game;
import at.fhv.game.model.Position;
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

    private List<Sabotage> resetSabotagePositions(List<Sabotage> sabotages){
        for (Sabotage s : sabotages){
            s.setPosition(new Position(-1, -1));
        }
        return sabotages;
    }

    public void addSabotagesToGame(Game game) {
        List<Sabotage> sabotages;
        sabotages = resetSabotagePositions(getAllSabotages());
        game.setSabotages(sabotages);
        //System.out.println("Sabotages after adding them to game in SabotageService: ");
        /*for (Sabotage s: game.getSabotages()){
            System.out.println("Sabotage id: " + s.getId() +
                    ", Position: " + s.getPosition().getX() + ", " + s.getPosition().getY());
        }*/
    }
}
