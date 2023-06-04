package documents;

import subscribers.*;

import java.sql.Timestamp;

public class DVD extends AbstractDocument {

    private static final int AGE_MINIMUM = 16;
    private final boolean adulte;

    /**
     * Generate a new Document
     * @param numero          the Document number
     * @param titre           the Document title
     * @param empruntePar     the subscriber who borrowed the Document
     * @param reservePar      the subscriber who reserved the Document
     * @param reservationTimeStamp the time when the Document was reserved
     */
    public DVD(int numero, String titre, Abonne empruntePar, Abonne reservePar, Timestamp reservationTimeStamp, DocumentStates state,boolean adulte) {
        super(numero, titre, empruntePar, reservePar, reservationTimeStamp, state);
        this.adulte = adulte;
    }

    @Override
    public void reservation(Abonne abonne) {
        if (isTooYoung(abonne) && this.adulte) {
            throw new SubscriberTooYoungException();
        }
        super.reservation(abonne);
    }

    @Override
    public void emprunt(Abonne abonne) {
        if (isTooYoung(abonne) && this.adulte) {
            throw new SubscriberTooYoungException();
        }
        super.emprunt(abonne);
    }



    /**
     * Return true if the subscriber is an adult
     * @param ab the subscriber
     * @return true if the subscriber is an adult
     */
    private static boolean isTooYoung(Abonne ab) {
        return ab.getAge() < AGE_MINIMUM;
    }
}