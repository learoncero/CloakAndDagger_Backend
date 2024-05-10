package at.fhv.minigames.service;

import at.fhv.minigames.model.DecipherSymbolsMiniGame;
import at.fhv.minigames.repository.DecipherSymbolsMiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class DecipherSymbolsMiniGameService {
    private final DecipherSymbolsMiniGameRepository decipherSymbolsMiniGameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public DecipherSymbolsMiniGameService(DecipherSymbolsMiniGameRepository decipherSymbolsMiniGameRepository) {
        this.decipherSymbolsMiniGameRepository = decipherSymbolsMiniGameRepository;
    }

    public DecipherSymbolsMiniGame getInstance(String gameCode, int taskId) {
        return decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId);
    }

    public List<String> createShuffledSymbols(List<String> symbols, int currentRound) {
        shuffleList(symbols);

        System.out.println("Shuffled symbols: " + symbols);

        Map<Integer, Integer> roundEndings = Map.of(
                1, 9,
                2, 16,
                3, 25
        );

        int endIndex = roundEndings.getOrDefault(currentRound, symbols.size());
        return symbols.subList(0, endIndex);
    }

    public static void shuffleList(List<String> list) {
        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            int randomIndex = random.nextInt(list.size());
            String temp = list.get(i);
            list.set(i, list.get(randomIndex));
            list.set(randomIndex, temp);
        }
    }

    public String getCorrectSymbol(List<String> symbols) {
        if (!symbols.isEmpty()) {
            Random rand = new Random();
            return symbols.get(rand.nextInt(symbols.size()));
        }

        return null;
    }

    public boolean verifySymbol(String symbol, int taskId, String gameCode, int currentRound) {
        DecipherSymbolsMiniGame decipherSymbolsMiniGame = decipherSymbolsMiniGameRepository.getInstance(gameCode, taskId);
        if (decipherSymbolsMiniGame != null) {
            String correctSymbol = decipherSymbolsMiniGame.getCorrectSymbol();

            if (correctSymbol.equals(symbol)) {
                if (currentRound == 3) {
                    System.out.println("Game done");
                    restTemplate.postForEntity("http://localhost:5010/api/game/task/" + gameCode + "/done", taskId, Void.class);
                    deleteInstance(gameCode, taskId);
                }

                return true;
            }
        }

        return false;
    }

    public void saveNewInstance(String gameCode, int taskId, DecipherSymbolsMiniGame miniGame) {
        decipherSymbolsMiniGameRepository.saveInstance(gameCode, taskId, miniGame);
    }

    public void deleteInstance(String gameCode, int taskId) {
        decipherSymbolsMiniGameRepository.removeInstance(gameCode, taskId);
    }
}
