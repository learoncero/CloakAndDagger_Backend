package at.fhv.minigames.controller;

import at.fhv.minigames.model.messages.MovingSquareMiniGameMessage;
import at.fhv.minigames.service.MovingSquareMiniGameService;
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
public class MovingSquareMiniGameController {
    private final MovingSquareMiniGameService movingSquareMiniGameService;

    @Autowired
    public MovingSquareMiniGameController(MovingSquareMiniGameService movingSquareMiniGameService) {
        this.movingSquareMiniGameService = movingSquareMiniGameService;
    }

    @Operation(summary = "Mark a moving square task as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task marked as completed successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/movingSquare/{gameCode}/markAsCompleted")
    public ResponseEntity<Boolean> markAsCompleted(@PathVariable("gameCode") String gameCode, @RequestBody MovingSquareMiniGameMessage submission) {

        if (movingSquareMiniGameService.markAsCompleted(gameCode, submission.getTaskId())) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
