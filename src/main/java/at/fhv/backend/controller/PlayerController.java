package at.fhv.backend.controller;

import at.fhv.backend.model.*;
import at.fhv.backend.service.GameService;
import at.fhv.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public PlayerController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @MessageMapping("/join")
    @SendTo("/topic/playerJoin")
    public Player createPlayer(@Payload PlayerJoinMessage joinMessage) {
        System.out.println("Received join message: " + joinMessage.getId() + " " + joinMessage.getUsername() + " " + joinMessage.getPosition().getX() + " " + joinMessage.getPosition().getY());
        Game game = gameService.getGameByCode(joinMessage.getGameCode());
        Position position = new Position(joinMessage.getPosition().getX(), joinMessage.getPosition().getY());
        return playerService.createPlayer(joinMessage.getId(), joinMessage.getUsername(), position, game);
    }

    @MessageMapping("/move")
    @SendTo("/topic/positionChange")
    public Player movePlayer(@Payload PlayerMoveMessage moveMessage) {
        System.out.println("Received move message: " + moveMessage.getId() + " to " + moveMessage.getNewPosition().getX() + ", " + moveMessage.getNewPosition().getY());
        int playerID = moveMessage.getId();
        Position newPosition = new Position(moveMessage.getNewPosition().getX(), moveMessage.getNewPosition().getY());
        System.out.println("newPosition received: "+newPosition.getX()+", "+newPosition.getY());
        if (playerID != 0) {
            System.out.println("Updating player position in controller");
            playerService.updatePlayerPosition(playerID, newPosition);
        }

        return playerService.getPlayerByID(playerID);
    }


}
