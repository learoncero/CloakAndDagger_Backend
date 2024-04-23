package at.fhv.backend.model;

public class Player {
    private static int idCounter = 1;
    private int id;
    private String username;
    private Position position;
    private Game game;
    private Role role;
    private boolean isMirrored;
    private boolean isMoving;

    public Player(String username, Position position, Game game) {
        this.id = idCounter++;
        this.username = username;
        this.position = position;
        this.game = game;
        this.role = Role.CREWMATE;
        this.isMirrored = false;
        this.isMoving = false;
    }

    public Player(String username, Position position) {
        this.id = idCounter++;
        this.username = username;
        this.position = position;
        this.game = new Game();
        this.role = Role.CREWMATE;
        this.isMirrored = false;
        this.isMoving = false;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
}
