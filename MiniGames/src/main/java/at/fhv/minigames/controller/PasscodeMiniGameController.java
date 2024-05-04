package at.fhv.minigames.controller;

import at.fhv.minigames.service.PasscodeMiniGameService;
import at.fhv.minigames.model.PasscodeMiniGame;
import at.fhv.minigames.model.messages.PasscodeMiniGameMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class PasscodeMiniGameController {
    private final PasscodeMiniGameService passcodeMiniGameService;

    @Autowired
    public PasscodeMiniGameController(PasscodeMiniGameService passcodeMiniGameService) {
        this.passcodeMiniGameService = passcodeMiniGameService;
    }

    @Operation(summary = "Add value to passcode sum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Value added to passcode sum successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/passcode/{gameCode}/add")
    public ResponseEntity<Integer> addToSum(@RequestBody PasscodeMiniGameMessage passcodeMiniGameMessage, @PathVariable("gameCode") String gameCode) {
        int currentValue = passcodeMiniGameMessage.getValue();
        int taskId = passcodeMiniGameMessage.getTaskId();
        int currentSum = passcodeMiniGameService.addToSum(currentValue, taskId, gameCode);

        if (currentSum == -1) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(currentSum);
        }
    }

    @Operation(summary = "Get random passcode sum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random passcode sum retrieved successfully")
    })
    @PostMapping("/passcode/{gameCode}/random")
    public ResponseEntity<Integer> getRandomSum(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        PasscodeMiniGame passcodeMiniGame = passcodeMiniGameService.getInstance(gameCode, taskId);
        return ResponseEntity.ok(passcodeMiniGame.getRandomSum());
    }

    @Operation(summary = "Reset passcode sum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passcode sum reset successfully")
    })
    @PostMapping("/passcode/{gameCode}/reset")
    public ResponseEntity<Integer> resetSum(@PathVariable("gameCode") String gameCode, @RequestBody int taskId) {
        PasscodeMiniGame passcodeMiniGame = passcodeMiniGameService.getInstance(gameCode, taskId);
        passcodeMiniGame.setCurrentSum(0);
        return ResponseEntity.ok(0);
    }
}
