package at.fhv.game.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Player {
    private static int idCounter = 1;
    @Schema(description = "The unique identifier for the player")
    private int id;

    @Schema(description = "The username of the player")
    private String username;

    @Schema(description = "The position of the active player")
    private Position playerPosition;

    @Schema(description = "The position of the dead player body")
    private Position deadBodyPosition;

    @Schema(description = "The game the player is associated with")
    private Game game;

    @Schema(description = "The role of the player")
    private Role role;

    @Schema(description = "Flag indicating if the player's actions are mirrored")
    private boolean isMirrored;

    @Schema(description = "Flag indicating if the player is currently moving")
    private boolean isMoving;
    @Schema(description = "The color of the Player")
    private String playerColor;


    public Player(String username, Position playerPosition, Game game, String playerColor) {
        this.id = idCounter++;
        this.username = username;
        this.playerPosition = playerPosition;
        this.deadBodyPosition = new Position(-1, -1);
        this.game = game;
        this.role = Role.CREWMATE;
        this.isMirrored = false;
        this.isMoving = false;
        this.playerColor = playerColor;
    }

    public Player() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Position playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Position getDeadBodyPosition() {
        return deadBodyPosition;
    }

    public void setDeadBodyPosition(Position deadBodyPosition) {
        this.deadBodyPosition = deadBodyPosition;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isMirrored() {
        return isMirrored;
    }

    public void setMirrored(boolean mirrored) {
        this.isMirrored = mirrored;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
