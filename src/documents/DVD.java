package documents;

import subscribers.Abonne;

import java.time.LocalTime;

public class DVD extends DocumentModel{
    private final boolean adulte;

    /**
     * Generate a new Document
     * @param numero          the Document number
     * @param titre           the Document title
     * @param empruntePar     the subscriber who borrowed the Document
     * @param reservePar      the subscriber who reserved the Document
     * @param reservationTime the time when the Document was reserved
     */
    public DVD(int numero, String titre, Abonne empruntePar, Abonne reservePar, LocalTime reservationTime, boolean adulte) {
        super(numero, titre, empruntePar, reservePar, reservationTime);
        this.adulte = adulte;
    }

    /**
     * Return if the DVD is for adults only
     * @return true if the DVD is for adults only
     */
    public boolean isForAdults() {
        return this.adulte;
    }
}