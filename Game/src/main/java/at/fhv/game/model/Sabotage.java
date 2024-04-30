package at.fhv.game.model;

public class Sabotage {
    private int id;
    private String title;
    private String description;
    private Position position = new Position();

    public Sabotage(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.position.setX(-1);
        this.position.setY(-1);
    }

    public Sabotage() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
