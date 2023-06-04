package server.log;

import java.sql.Timestamp;

public class Log {
    private final Actions action;
    private final int numeroAbonne;
    private final int numeroDocument;
    private final Timestamp timeStamp;

    /**
     * Create a Log object and print the log in the console
     * @param action the Log Action
     * @param numeroAbonne the abonne number
     * @param numeroDocument the document number
     * @param timeStamp the Log timeStamp
     */
    public Log(Actions action, int numeroAbonne, int numeroDocument, Timestamp timeStamp) {
        this.action = action;
        this.numeroAbonne = numeroAbonne;
        this.numeroDocument = numeroDocument;
        this.timeStamp = timeStamp;
        System.out.println(this);
    }

    /**
     * Get the Action
     * @return the string Action
     */
    public String getAction() {
        return action.toString();
    }

    /**
     * Get the abonne number
     * @return the abonne number
     */
    public int getNumeroAbonne() {
        return numeroAbonne;
    }

    /**
     * Get the document number
     * @return the document number
     */
    public int getNumeroDocument() {
        return numeroDocument;
    }

    /**
     * Get the timeStamp
     * @return timeStamp
     */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }
    @Override
    public String toString() {
        return "[" + timeStamp + "] " + " - Action: " + action + ", Numéro abonné: " + numeroAbonne +
                ", Numéro document: " + numeroDocument;
    }
}