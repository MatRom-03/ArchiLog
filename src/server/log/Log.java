package server.log;

import java.sql.Timestamp;

public class Log {
    private final Actions action;
    private final int numeroAbonne;
    private final int numeroDocument;
    private final Timestamp timeStamp;

    public Log(Actions action, int numeroAbonne, int numeroDocument, Timestamp timeStamp) {
        this.action = action;
        this.numeroAbonne = numeroAbonne;
        this.numeroDocument = numeroDocument;
        this.timeStamp = timeStamp;
        System.out.println(this);
    }

    public String getAction() {
        return action.toString();
    }

    public int getNumeroAbonne() {
        return numeroAbonne;
    }

    public int getNumeroDocument() {
        return numeroDocument;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
    @Override
    public String toString() {
        return "[" + timeStamp + "] " + " - Action: " + action + ", Numéro abonné: " + numeroAbonne +
                ", Numéro document: " + numeroDocument;
    }
}