package at.fhv.backend.model;

public class Player {
    private int id;
    private String username;
    private Position position;

    public Player(int id, String username, int x, int y) {
        this.id = id;
        this.username = username;
        this.position = new Position(x, y);
    }

    public Player (int id, String username) {
        this.id = id;
        this.username = username;
        position = new Position(0, 0);
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
