package documents;

import subscribers.Subscriber;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DVD implements Document{
    private final int numero;
    private final String titre;
    private final boolean adulte;
    private Subscriber empruntePar;
    private Subscriber reservePar;
    private LocalTime reservationTime;


    /**
     * Generate a new DVD
     * @param numero the DVD number
     * @param titre the DVD title
     * @param adulte true if the DVD is for adults only
     * @param empruntePar the subscriber who borrowed the DVD
     * @param reservePar the subscriber who reserved the DVD
     */
    public DVD(int numero, String titre, boolean adulte, Subscriber empruntePar, Subscriber reservePar, LocalTime reservationTime) {
        this.numero = numero;
        this.titre = titre;
        this.adulte = adulte;
        this.empruntePar = empruntePar;
        this.reservePar = reservePar;
        this.reservationTime = reservationTime;
    }

    /**
     * Return the DVD number
     * @return numero
     */
    @Override
    public int numero() {
        return this.numero;
    }

    /**
     * Return the Subscriber who borrowed the DVD
     * @return suscriber
     */
    @Override
    public Subscriber empruntePar() {
        return this.empruntePar;
    }

    /**
     * Return the Subscriber who reserved the DVD
     * @return suscriber
     */
    @Override
    public Subscriber reservePar() {
        return this.reservePar;
    }

    /**
     * Do a reservation for the DVD by the subscriber
     * @param ab the subscriber who wants to reserve the DVD
     */
    @Override
    public void reservation(Subscriber ab) {
        if (empruntePar == null && reservePar == null) {
            reservePar = ab;
            this.reservationTime = LocalTime.now().plus(2, java.time.temporal.ChronoUnit.HOURS);
        }
    }

    /**
     * Do a loan for the DVD by the subscriber
     * @param ab the subscriber who wants to borrow the DVD
     */
    @Override
    public void emprunt(Subscriber ab) {
        if (reservePar == null) {
            empruntePar = ab;
        } else if (reservePar == ab) {
            empruntePar = ab;
            reservePar = null;
        }
    }

    /**
     * Return the DVD
     */
    @Override
    public void retour() {
        empruntePar = null;
    }

    @Override
    public String toString() {
        // TODO Modify the display the DVD for the catalog
        return "DVD [numero=" + numero + ", titre=" + titre + ", adulte=" + adulte + ", empruntePar=" + empruntePar + ", reservePar=" + reservePar + ", reservationTime=" + this.reservationTime + "]";
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
            this.reservePar = null;
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
     * Return if the DVD is for adults only
     * @return true if the DVD is for adults only
     */
    public boolean isForAdults() {
        return this.adulte;
    }
}
