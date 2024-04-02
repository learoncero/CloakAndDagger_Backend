package at.fhv.backend.model;

public class Player {
    private int id;
    private String username;
    private Position position;
    private Game game;

    public Player(int id, String username, Position position, Game game) {
        this.id = id;
        this.username = username;
        this.position = position;
        this.game = game;
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
}
