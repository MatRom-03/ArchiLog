package sqlData;

import documents.*;
import subscribers.Abonne;
import subscribers.SubscriberTooYoungException;

import java.sql.*;
import java.util.Map;

public class Media_library {
    private static final int AGE_MINIMUM = 16;

    /**
     * Constructor of the class, it will get all the data from the database.
     * @throws SQLException if the database is not found
     */
    public static void initMedia_library() throws SQLException {
        DataManager.getDataFromFile();
    }

    /**
     * Reserve a document and update the database
     * @param numeroDocument the document number
     * @param numeroAbonne the subscriber number
     * @throws SQLException if the database is not found
     */
    public synchronized static void reservation(int numeroDocument, int numeroAbonne) throws SQLException, DocumentNotAvailableException, SubscriberTooYoungException {
        DocumentModel document = DataManager.documentsMap.get(numeroDocument);
        Abonne abonne = DataManager.abonnesMap.get(numeroAbonne);
        if(document == null)
            return;
        if (document.reservePar() != null)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), document.reservePar() != null);
        if (document.empruntePar() != null)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), false);
        if (document.getClass() == DVD.class && ((DVD) document).isForAdults() && isTooYoung(abonne))
            throw new SubscriberTooYoungException();
        document.reservation(abonne);
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());

        System.out.println("Reservation du document " + numeroDocument + " par l'abonne " + numeroAbonne);
        DataManager.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }

    /**
     * Emprunt a document and update the database
     * @param numeroDocument the document number
     * @param numeroAbonne the subscriber number
     * @throws SQLException if the database is not found
     */
    public synchronized static void emprunt(int numeroDocument, int numeroAbonne) throws SQLException, DocumentNotAvailableException, SubscriberTooYoungException {
        DocumentModel document = DataManager.documentsMap.get(numeroDocument);
        Abonne abonne = DataManager.abonnesMap.get(numeroAbonne);
        if (document == null)
            return;
        if (document.reservePar() != null && document.reservePar().getNumero() != numeroAbonne)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), document.reservePar() != null);
        if (document.empruntePar() != null)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), false);
        if (document.getClass() == DVD.class && ((DVD) document).isForAdults() && isTooYoung(abonne))
            throw new SubscriberTooYoungException();
        document.emprunt(abonne);
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());

        System.out.println("Emprunt du document " + numeroDocument + " par l'abonne " + numeroAbonne);
        DataManager.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }

    /**
     * Return a document and update the database
     * @param numeroDocument the document number
     * @throws SQLException if the database is not found
     */
    public synchronized static void retour(int numeroDocument) throws SQLException {
        DocumentModel document = DataManager.documentsMap.get(numeroDocument);
        if (document == null)
            return;
        document.retour();
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());

        System.out.println("Retour du document " + numeroDocument);
        DataManager.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }

    /**
     * Return the catalog of all documents
     * @return catalog of documents
     */
    public static String getCatalogue() {
        StringBuilder catalog = new StringBuilder("=============== Catalogue ===============\n");
        for (Map.Entry<Integer, DocumentModel> entry : DataManager.documentsMap.entrySet()) {
            catalog.append(entry.getValue().toString()).append("\n");
        }
        catalog.append("=========================================");
        return catalog.toString();
    }

    /**
     * Verify if the numeroDocument refers to an existing document
     * @param numeroDocument the document number
     * @return true if existed, false otherwise
     */
    public static boolean documentNotExist(int numeroDocument) {
        return !DataManager.documentsMap.containsKey(numeroDocument);
    }

    /**
     * Verify if the numeroSubscriber refers to an existing subscriber
     * @param numeroSubscriber abonne number
     * @return true if existed, false otherwise
     */
    public static boolean abonneNotExist(int numeroSubscriber) {
        return !DataManager.abonnesMap.containsKey(numeroSubscriber);
    }

    public static int getNbLignesCatalogue() {
        return DataManager.documentsMap.size() + 2; // +2 for the header and footer
    }

    /**
     * Return true if the subscriber is an adult
     * @param ab the subscriber
     * @return true if the subscriber is an adult
     */
    private static boolean isTooYoung(Abonne ab) {
        return ab.getAge() < AGE_MINIMUM;
    }

    /**
     *
     * @param numeroDocument the document number
     * @return true if the document is not borrowed
     */
    public static boolean isNotBorrowed(int numeroDocument) {
        return DataManager.documentsMap.get(numeroDocument).empruntePar() == null;
    }

    /**
     * Remove Booking
     * @param numeroDocument the document number
     */
    public synchronized static void removeBooking(int numeroDocument) {
        DocumentModel document = DataManager.documentsMap.get(numeroDocument);
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());
        System.out.println("=========================================");
        System.out.println("Fin du timer pour la reservation du document : " + numeroDocument);
    }
}
