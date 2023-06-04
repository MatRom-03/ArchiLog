package documents;

import documents.exceptions.DocumentAlreadyBookedException;
import documents.exceptions.DocumentAlreadyBorrowedException;
import documents.exceptions.GrandChamanException;
import server.logic.Media_library;
import subscribers.Abonne;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Timer;

public abstract class AbstractDocument implements Document{

    private final int numero;
    private final String titre;
    private Abonne empruntePar;
    private Abonne reservePar;
    private Timestamp reservationTimeStamp;
    private ArrayList<Abonne> waitingList = new ArrayList<>();
    private DocumentStates state;

    private final Timer timer;

    /**
     * Generate a new Document
     * @param numero the Document number
     * @param titre the Document title
     * @param empruntePar the subscriber who borrowed the Document
     * @param reservePar the subscriber who reserved the Document
     */
    public AbstractDocument(int numero, String titre, Abonne empruntePar, Abonne reservePar, Timestamp reservationTimeStamp, DocumentStates state) {
        this.numero = numero;
        this.titre = titre;
        this.empruntePar = empruntePar;
        this.reservePar = reservePar;
        this.reservationTimeStamp = reservationTimeStamp;
        this.state = state;
        this.timer = new Timer();
        if (getRemainingReservationTime() <= 0 && state == DocumentStates.RESERVED){
            this.reservePar = null;
            this.state = DocumentStates.AVAILABLE;
        } else if (getRemainingReservationTime() > 0 && state == DocumentStates.RESERVED){
            setTimerAtStart();
        }
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
        if (this.reservePar != null) {
            if (this.getRemainingReservationTime() >= 60)
                throw new DocumentAlreadyBookedException(this.numero, this.getRemainingReservationTime());
            else
                throw new GrandChamanException(this.numero);
        }
        if (this.empruntePar != null)
            throw new DocumentAlreadyBorrowedException(this.numero);

        this.reservePar = ab;
        this.reservationTimeStamp = Timestamp.valueOf(new Timestamp(System.currentTimeMillis()).toLocalDateTime().plusHours(2));
        this.state = DocumentStates.RESERVED;
        setTimer();
    }

    /**
     * Do a loan for the Document by the subscriber
     * @param ab the subscriber who wants to borrow the Document
     */
    @Override
    public void emprunt(Abonne ab) {

        if (this.reservePar != null && this.reservePar != ab)
            throw new DocumentAlreadyBookedException(this.numero, this.getRemainingReservationTime());
        if (this.empruntePar != null)
            throw new DocumentAlreadyBorrowedException(this.numero);

        this.empruntePar = ab;
        this.reservePar = null;
        this.state = DocumentStates.BORROWED;
        this.timer.cancel();
    }

    /**
     * Return the Document
     */
    @Override
    public void retour() {
        this.empruntePar = null;
        this.state = DocumentStates.AVAILABLE;
    }

    @Override
    public String toString() {

        String formattedNumero = String.format("%04d", numero);
        String formattedTitre = titre.length() > 30 ? titre.substring(0, 27) + "..." : titre;
        String state = String.format("%-10s", this.state.toString());


        return String.format("%-11s %-5s %-31s", state,formattedNumero, formattedTitre);
    }

    /**
     * Return the remaining time of the reservation
     * @return the remaining time of the reservation
     */
    public int getRemainingReservationTime() {
        if(this.reservePar == null)
            return 0;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long timeLeft = currentTime.toLocalDateTime().until(this.reservationTimeStamp.toLocalDateTime(), ChronoUnit.SECONDS);
        if (timeLeft < 0) {
            return 0;
        }
        return (int) timeLeft;
    }

    /**
     * Return the reservation time
     *
     * @return the reservation time
     */
    public Timestamp getReservationTimeStamp() {
        return this.reservationTimeStamp;
    }


    /**
     * Set timer for reservation, 2 hours
     * at the end of the timer, the reservation is canceled.
     */
    public void setTimer() {
        this.reservationTimeStamp = Timestamp.valueOf(new Timestamp(System.currentTimeMillis()).toLocalDateTime().plusHours(2));
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                reservePar = null;
                state = DocumentStates.AVAILABLE;
                Media_library.removeBooking(numero);
            }
        }, 2*60*60*1000);
    }

    /**
     * Add a subscriber to the waiting list
     * @param ab the subscriber to add
     */
    public void addWaitingList(Abonne ab) {
        this.waitingList.add(ab);
    }

    /**
     * Get the waiting list
     * @return the waiting list
     */
    public ArrayList<Abonne> getWaitingList() {
        return waitingList;
    }

    /**
     * Get the title of the document
     * @return the title of the document
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Get the state of the document
     * @return the state of the document
     */
    public DocumentStates getState() {
        return state;
    }

    /**
     * Set a special timer for the reservation
     * at the end of the timer, the reservation is canceled.
     */
    public void setTimerAtStart() {
        this.timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                reservePar = null;
                state = DocumentStates.AVAILABLE;
                Media_library.removeBooking(numero);
            }
        }, getRemainingReservationTime() * 1000L);
    }
}