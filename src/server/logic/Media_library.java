package server.logic;

import documents.*;
import server.log.Actions;
import server.log.Log;
import sqlAndData.DataManager;
import subscribers.Abonne;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class Media_library {

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
    public synchronized static void reservation(int numeroDocument, int numeroAbonne) throws SQLException {
        AbstractDocument document = DataManager.documentsMap.get(numeroDocument);
        Abonne abonne = DataManager.abonnesMap.get(numeroAbonne);
        if(document == null)
            return;
        document.reservation(abonne);
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTimeStamp(), document.getState());
        DataManager.insertLog(new Log(Actions.BOOKING, numeroAbonne, numeroDocument, new Timestamp(System.currentTimeMillis())));
    }

    /**
     * Emprunt a document and update the database
     * @param numeroDocument the document number
     * @param numeroAbonne the subscriber number
     * @throws SQLException if the database is not found
     */
    public synchronized static void emprunt(int numeroDocument, int numeroAbonne) throws SQLException {
        AbstractDocument document = DataManager.documentsMap.get(numeroDocument);
        Abonne abonne = DataManager.abonnesMap.get(numeroAbonne);
        if (document == null)
            return;

        document.emprunt(abonne);
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTimeStamp(), document.getState());
        DataManager.insertLog(new Log(Actions.BORROW, numeroAbonne, numeroDocument, new Timestamp(System.currentTimeMillis())));
    }

    /**
     * Return a document and update the database
     * @param numeroDocument the document number
     * @throws SQLException if the database is not found
     */
    public synchronized static void retour(int numeroDocument) throws SQLException {
        AbstractDocument document = DataManager.documentsMap.get(numeroDocument);
        if (document == null)
            return;
        document.retour();
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTimeStamp(), document.getState());
        DataManager.insertLog(new Log(Actions.RETURN, 0, numeroDocument, new Timestamp(System.currentTimeMillis())));
    }

    /**
     * Return the catalog of all documents
     * @return catalog of documents
     */
    public static String getCatalogue() {
        StringBuilder catalog = new StringBuilder("=============== Catalogue ===============\n");
        for (Map.Entry<Integer, AbstractDocument> entry : DataManager.documentsMap.entrySet()) {
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
        AbstractDocument document = DataManager.documentsMap.get(numeroDocument);
        DataManager.updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTimeStamp(), document.getState());
        DataManager.insertLog(new Log(Actions.REMOVEBOOKING, 0, numeroDocument, new Timestamp(System.currentTimeMillis())));
    }

    public static int getRemainingReservationTime(int numeroDocument) {
        return DataManager.documentsMap.get(numeroDocument).getRemainingReservationTime();
    }

    public static ArrayList<Abonne> getWaitingList(int numeroDocument) {
        return DataManager.documentsMap.get(numeroDocument).getWaitingList();
    }

    public static AbstractDocument getDocument(int numeroDocument) {
        return DataManager.documentsMap.get(numeroDocument);
    }

    public static void addAlert(int numeroDocument, int numeroAbonne) {
        AbstractDocument document = DataManager.documentsMap.get(numeroDocument);
        Abonne abonne = DataManager.abonnesMap.get(numeroAbonne);
        document.addWaitingList(abonne);
    }
}