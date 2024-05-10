package at.fhv.minigames.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DecipherSymbolsMiniGame extends MiniGame {
    @Schema (description = "List of symbols for the mini-game")
    private List<String> symbols = Arrays.asList("⥊", "⥋", "⥌", "⥍", "⥎", "⥏", "⥐", "⥑", "⥒", "⥓", "⥔", "⥕", "⥖", "⥗", "⥘", "⥙", "⥚", "⥛", "⥜", "⥝", "⥞", "⥟", "⥠", "⥡", "⥦");

    @Schema (description = "Shuffled list of symbols for the mini-game")
    private List<String> shuffledSymbols;

    @Schema (description = "Symbol to be deciphered")
    private String correctSymbol;

    public DecipherSymbolsMiniGame(int id, String title, String description) {
        super(id, title, description);
    }
}