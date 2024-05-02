package at.fhv.tasks.service;

import at.fhv.tasks.model.ColorSeqMiniGame;
import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.model.PasscodeMiniGame;
import at.fhv.tasks.repository.MiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiniGameService {
    private final MiniGameRepository miniGameRepository;

    @Autowired
    public MiniGameService(MiniGameRepository miniGameRepository) {
        this.miniGameRepository = miniGameRepository;
    }

    public List<MiniGame> getAllMiniGames() {
        return miniGameRepository.getAllMiniGames();
    }

    public MiniGame getMiniGameById(int id) {
        return miniGameRepository.getMiniGameById(id);
    }

    public MiniGame cloneMiniGame(MiniGame miniGameTemplate) {
        MiniGame clone = null;
        if (miniGameTemplate instanceof PasscodeMiniGame) {
            PasscodeMiniGame originalPasscodeGame = (PasscodeMiniGame) miniGameTemplate;
            PasscodeMiniGame clonedPasscodeGame = new PasscodeMiniGame();

            // Copy properties from original to cloned mini-game
            clonedPasscodeGame.setId(originalPasscodeGame.getId());
            clonedPasscodeGame.setTitle(originalPasscodeGame.getTitle());
            clonedPasscodeGame.setDescription(originalPasscodeGame.getDescription());
            clonedPasscodeGame.setCurrentSum(originalPasscodeGame.getCurrentSum());
            clonedPasscodeGame.setRandomSum(originalPasscodeGame.getRandomSum());

            clone = clonedPasscodeGame;

        } else if(miniGameTemplate instanceof ColorSeqMiniGame) {
            ColorSeqMiniGame originalColorSeqGame = (ColorSeqMiniGame) miniGameTemplate;
            ColorSeqMiniGame clonedColorSeqGame = new ColorSeqMiniGame();

            // Copy properties from original to cloned mini-game
            clonedColorSeqGame.setId(originalColorSeqGame.getId());
            clonedColorSeqGame.setTitle(originalColorSeqGame.getTitle());
            clonedColorSeqGame.setDescription(originalColorSeqGame.getDescription());
            clonedColorSeqGame.setShuffledColors(originalColorSeqGame.getShuffledColors());

            clone = clonedColorSeqGame;

        }

        return clone;
    }
}
