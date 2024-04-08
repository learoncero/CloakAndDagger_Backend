package at.fhv.backend.controller;

import at.fhv.backend.model.*;
import at.fhv.backend.service.GameService;
import at.fhv.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;

@Controller
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @MessageMapping("/move")
    @SendTo("/topic/positionChange")
    public Player movePlayer(@Payload String keyCode, @CookieValue(value = "playerId", defaultValue = "0") int playerId) {
        Player player = playerService.getPlayerByID(playerId);

        if (player != null) {
            Position newPosition = calculateNewPosition(player.getPosition(), keyCode);
            playerService.updatePlayerPosition(playerId, newPosition);
            return playerService.getPlayerByID(playerId);
        }

        return null;
    }

    private Position calculateNewPosition(Position currentPosition, String keyCode) {
        //System.out.println("Current position: " + currentPosition.getX() + ", " + currentPosition.getY() + " Key code: " + keyCode);
        int deltaX = 0, deltaY = 0;
        switch (keyCode) {
            case "KeyA":
                deltaX = -1;
                break;
            case "KeyW":
                deltaY = -1;
                break;
            case "KeyD":
                deltaX = 1;
                break;
            case "KeyS":
                deltaY = 1;
                break;
            default: System.out.println("Invalid key code");
                break;
        }

        int newX = currentPosition.getX() + deltaX;
        int newY = currentPosition.getY() + deltaY;

        //System.out.println("New position: " + newX + ", " + newY);

        return new Position(newX, newY);
    }
}