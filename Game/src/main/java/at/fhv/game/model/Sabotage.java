package at.fhv.game.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Sabotage {
    @Schema(description = "The ID of the sabotage")
    private int id;

    @Schema(description = "The title of the sabotage")
    private String title;

    @Schema(description = "The description of the sabotage")
    private String description;

    @Schema(description = "The position of the sabotage")
    private Position position;

    public Sabotage(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.position = new Position(-1, -1);
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
