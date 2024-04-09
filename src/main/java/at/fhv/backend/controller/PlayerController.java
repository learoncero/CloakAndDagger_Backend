package at.fhv.backend.controller;

import at.fhv.backend.service.GameService;
import at.fhv.backend.service.PlayerService;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;

    public PlayerController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }
}