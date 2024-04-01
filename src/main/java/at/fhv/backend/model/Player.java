package at.fhv.backend.model;

public class Player {
    private int id;
    private String username;
    private Position position;
    private Game game;
    private static int nextID = 1;

    public Player(String username, int x, int y, Game game) {
        this.id = nextID++;
        this.username = username;
        this.position = new Position(x, y);
        this.game = game;
    }

    public Player (String username, Game game) {
        this(username, 0, 0, game);
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
