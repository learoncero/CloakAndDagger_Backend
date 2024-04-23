package at.fhv.backend.model;

public class PlayerActivity {
    private long lastMoveTime;
    private String gameCode;

    public PlayerActivity(long lastMoveTime, String gameCode) {
        this.lastMoveTime = lastMoveTime;
        this.gameCode = gameCode;
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }
}
