package at.fhv.backend.model;

public class Sabotage {
    private int id;
    private String title;
    private String description;

    public Sabotage(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}