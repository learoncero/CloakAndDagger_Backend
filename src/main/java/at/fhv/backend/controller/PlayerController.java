package at.fhv.backend.controller;

import at.fhv.backend.PlayerJoinMessage;
import at.fhv.backend.PlayerMoveMessage;
import at.fhv.backend.service.PlayerService;
import at.fhv.backend.model.Player;
import at.fhv.backend.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService, SimpMessagingTemplate messagingTemplate) {
        this.playerService = playerService;
    }

    @MessageMapping("/join")
    @SendTo("/topic/playerJoin")
    public Player createPlayer(@Payload PlayerJoinMessage joinMessage) {
        System.out.println("Received join message: " + joinMessage.getId() + " " + joinMessage.getUsername() + " " + joinMessage.getX() + " " + joinMessage.getY());
        return playerService.createPlayer(joinMessage.getId(), joinMessage.getUsername(), joinMessage.getX(), joinMessage.getY());
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
