package at.fhv.minigames.service;

import at.fhv.minigames.model.ColorSeqMiniGame;
import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import at.fhv.minigames.model.MiniGame;
import at.fhv.minigames.model.PasscodeMiniGame;
import at.fhv.minigames.repository.MiniGameRepository;
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

            clonedPasscodeGame.setId(originalPasscodeGame.getId());
            clonedPasscodeGame.setTitle(originalPasscodeGame.getTitle());
            clonedPasscodeGame.setDescription(originalPasscodeGame.getDescription());
            clonedPasscodeGame.setCurrentSum(originalPasscodeGame.getCurrentSum());
            clonedPasscodeGame.setRandomSum(originalPasscodeGame.getRandomSum());

            clone = clonedPasscodeGame;

        } else if (miniGameTemplate instanceof ColorSeqMiniGame) {
            ColorSeqMiniGame originalColorSeqGame = (ColorSeqMiniGame) miniGameTemplate;
            ColorSeqMiniGame clonedColorSeqGame = new ColorSeqMiniGame();

            clonedColorSeqGame.setId(originalColorSeqGame.getId());
            clonedColorSeqGame.setTitle(originalColorSeqGame.getTitle());
            clonedColorSeqGame.setDescription(originalColorSeqGame.getDescription());
            clonedColorSeqGame.setShuffledColors(originalColorSeqGame.getShuffledColors());

            clone = clonedColorSeqGame;
        } else if (miniGameTemplate instanceof DecipherSymbolsMiniGame) {
            DecipherSymbolsMiniGame originalDecipherSymbolsGame = (DecipherSymbolsMiniGame) miniGameTemplate;
            DecipherSymbolsMiniGame clonedDecipherSymbolsGame = new DecipherSymbolsMiniGame();

            clonedDecipherSymbolsGame.setId(originalDecipherSymbolsGame.getId());
            clonedDecipherSymbolsGame.setTitle(originalDecipherSymbolsGame.getTitle());
            clonedDecipherSymbolsGame.setDescription(originalDecipherSymbolsGame.getDescription());
            clonedDecipherSymbolsGame.setSymbols(originalDecipherSymbolsGame.getSymbols());

            clone = clonedDecipherSymbolsGame;
        }

        return clone;
    }
}
