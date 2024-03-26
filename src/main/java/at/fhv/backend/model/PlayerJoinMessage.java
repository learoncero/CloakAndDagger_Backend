package at.fhv.backend.model;

public class PlayerJoinMessage {
    private int id;
    private String username;
    private int x;
    private int y;

    public PlayerJoinMessage() {
    }

    public PlayerJoinMessage(int id, String username, int x, int y) {
        this.id = id;
        this.username = username;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}