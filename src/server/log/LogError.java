package server.log;

import java.sql.Timestamp;

public class LogError {

    private final Timestamp timeStamp;
    private final String message;

    public LogError(String message) {
        this.timeStamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
        System.out.println(this);
    }
    @Override
    public String toString() {
        return "[" + timeStamp + "] " + " - " + message;
    }
}
