package at.fhv.backend.controller;

import at.fhv.backend.model.Player;
import at.fhv.backend.model.PlayerMoveMessage;
import at.fhv.backend.model.Position;
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

    @MessageMapping("/move")
    @SendTo("/topic/positionChange")
    public Player movePlayer(@Payload PlayerMoveMessage moveMessage) {
        //System.out.println("Received move message: " + moveMessage.getId() + " to " + moveMessage.getNewPosition().getX() + ", " + moveMessage.getNewPosition().getY());
        int playerID = moveMessage.getId();
        Position newPosition = new Position(moveMessage.getNewPosition().getX(), moveMessage.getNewPosition().getY());
        //System.out.println("newPosition received: "+newPosition.getX()+", "+newPosition.getY());
        if (playerID != 0) {
            System.out.println("Updating player position in controller");
            playerService.updatePlayerPosition(playerID, newPosition);
        }
        return playerService.getPlayerByID(playerID);
    }


}
