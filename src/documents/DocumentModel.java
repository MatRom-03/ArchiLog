package documents;

import sqlData.Media_library;
import subscribers.Abonne;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;

public abstract class DocumentModel implements Document{
    private final int numero;
    private final String titre;
    private Abonne empruntePar;
    private Abonne reservePar;
    private LocalTime reservationTime;


    /**
     * Generate a new Document
     * @param numero the Document number
     * @param titre the Document title
     * @param empruntePar the subscriber who borrowed the Document
     * @param reservePar the subscriber who reserved the Document
     */
    public DocumentModel(int numero, String titre, Abonne empruntePar, Abonne reservePar, LocalTime reservationTime) {
        this.numero = numero;
        this.titre = titre;
        this.empruntePar = empruntePar;
        this.reservePar = reservePar;
        this.reservationTime = reservationTime;
    }

    /**
     * Return the Document number
     * @return numero
     */
    @Override
    public int numero() {
        return this.numero;
    }

    /**
     * Return the Subscriber who borrowed the Document
     * @return suscriber
     */
    @Override
    public Abonne empruntePar() {
        return this.empruntePar;
    }

    /**
     * Return the Subscriber who reserved the Document
     * @return suscriber
     */
    @Override
    public Abonne reservePar() {
        return this.reservePar;
    }

    /**
     * Do a reservation for the Document by the subscriber
     * @param ab the subscriber who wants to reserve the Document
     */
    @Override
    public void reservation(Abonne ab) {
        reservePar = ab;
        this.reservationTime = LocalTime.now().plus(2, java.time.temporal.ChronoUnit.HOURS);
        setTimer();
    }

    /**
     * Do a loan for the Document by the subscriber
     * @param ab the subscriber who wants to borrow the Document
     */
    @Override
    public void emprunt(Abonne ab) {
        empruntePar = ab;
        reservePar = null;
    }

    /**
     * Return the Document
     */
    @Override
    public void retour() {
        empruntePar = null;
    }

    @Override
    public String toString() {
        // TODO Modify the display the Document for the catalog
        return "Document [numero=" + numero + ", titre=" + titre + ", adulte=" + ", empruntePar=" + empruntePar + ", reservePar=" + reservePar + ", reservationTime=" + this.reservationTime + "]";
    }

    /**
     * Return the remaining time of the reservation
     * @return the remaining time of the reservation
     */
    public int getRemainingReservationTime() {
        if(this.reservePar == null)
            return 0;
        LocalTime currentTime = LocalTime.now();
        long timeLeft = currentTime.until(this.reservationTime, ChronoUnit.MINUTES);
        if (timeLeft < 0) {
            return 0;
        }
        return (int) timeLeft;
    }

    /**
     * Return the reservation time
     * @return the reservation time
     */
    public LocalTime getReservationTime() {
        return this.reservationTime;
    }


    /**
     * Set timer for reservation, 2 hours
     * at the end of the timer, the reservation is canceled
     */
    public void setTimer() {
        this.reservationTime = LocalTime.now().plus(5, ChronoUnit.SECONDS); // TODO: replace by 2h
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                reservePar = null;
                Media_library.removeBooking(numero);
            }
        }, 5*1000); // TODO: replace delay by 2*60*60*1000
    }
}
