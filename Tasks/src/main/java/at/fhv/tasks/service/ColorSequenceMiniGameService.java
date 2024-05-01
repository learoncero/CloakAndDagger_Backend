package at.fhv.tasks.service;

import at.fhv.tasks.model.ColorSeqMiniGame;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ColorSequenceMiniGameService {

    public List<String> createShuffledSequence(List<String> colors) {
        Collections.shuffle(colors);
        return colors;
    }

    public boolean verifySequence(List<String> submittedColors, List<String> shuffledColors) {
        return submittedColors.equals(shuffledColors);
    }
}
