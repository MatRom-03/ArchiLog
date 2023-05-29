package documents;

import subscribers.Subscriber;

public interface Document {
    int numero();

    // return null si pas emprunté ou pas réservé
    Subscriber empruntePar() ; // Abonné qui a emprunté ce document
    Subscriber reservePar() ; // Abonné qui a réservé ce document

    // precondition ni réservé ni emprunté
    void reservation(Subscriber ab) ;

    // precondition libre ou réservé par l’abonné qui vient emprunter
    void emprunt(Subscriber ab);

    // retour d’un document ou annulation d‘une réservation
    void retour();
}
