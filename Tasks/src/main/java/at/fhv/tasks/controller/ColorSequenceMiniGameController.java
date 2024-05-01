package at.fhv.tasks.controller;

import at.fhv.tasks.model.ColorSeqMiniGame;
import at.fhv.tasks.model.messages.ColorSeqMiniGameMessage;
import at.fhv.tasks.service.ColorSequenceMiniGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ColorSequenceMiniGameController {

    @Autowired
    private ColorSequenceMiniGameService service;

    @GetMapping("/colors/{gameCode}/start")
    public ResponseEntity<List<String>> startGame(@RequestBody ColorSeqMiniGameMessage ColorSeqMiniGameMessage, @PathVariable("gameCode") String gameCode) {
        Collections.shuffle(ColorSeqMiniGameMessage.getColors());
        List<String> sequence = service.createShuffledSequence(ColorSeqMiniGameMessage.getColors());

        return ResponseEntity.ok(sequence);
    }

    @PostMapping("/colors/{gameCode}/submit")
    public ResponseEntity<String> submitColors(@RequestBody ColorSeqMiniGameMessage submission) {
        if (service.verifySequence(submission.getColors(), submission.getShuffledColors())) {
            return ResponseEntity.ok("Task Completed");
        } else {
            return ResponseEntity.badRequest().body("Incorrect sequence");
        }
    }
}
