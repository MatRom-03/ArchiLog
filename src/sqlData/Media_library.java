package sqlData;

import documents.DocumentNotAvailableException;
import subscribers.Subscriber;
import documents.DVD;
import documents.Document;
import subscribers.SubscriberTooYoungException;

import java.nio.file.FileSystems;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Media_library {
    private static final int AGE_MINIMUM = 16;
    private Map<Integer, Subscriber> abonnesMap = new HashMap<>();
    private Map<Integer, Document> documentsMap = new HashMap<>();
    private final String pathDatabase = "jdbc:sqlite:"+ FileSystems.getDefault().getPath("src/sqlData/Data.db").normalize().toAbsolutePath().toString();

    /**
     * Constructor of the class, it will get all the data from the database.
     * @throws SQLException if the database is not found
     */
    public Media_library() throws SQLException {
        getDataFromFile();
    }

    /**
     * Retreve all the data from the database
     * @throws SQLException if the database is not found
     */
    private void getDataFromFile() throws SQLException {
        Connection connection = DriverManager.getConnection(this.pathDatabase);

        Statement statement = connection.createStatement();

        String queryAbonnes = "SELECT * FROM Abonnes";
        ResultSet resultSetAbonnes = statement.executeQuery(queryAbonnes);

        while (resultSetAbonnes.next()) {
            int numeroAbonne = resultSetAbonnes.getInt("numero");
            String name = resultSetAbonnes.getString("name");
            Date dateOfBirth = resultSetAbonnes.getDate("dateOfBirth");
            this.abonnesMap.put(numeroAbonne, new Subscriber(numeroAbonne, name, dateOfBirth));
        }


        String queryDVD = "SELECT * FROM DVD";
        ResultSet resultSetDVD = statement.executeQuery(queryDVD);

        while (resultSetDVD.next()) {
            int numeroDVD = resultSetDVD.getInt("numero");
            String titre = resultSetDVD.getString("titre");
            boolean adulte = resultSetDVD.getBoolean("adulte");
            int empruntePar = resultSetDVD.getInt("empruntePar");
            int reservePar = resultSetDVD.getInt("reservePar");
            LocalTime reservationTime = resultSetDVD.getTime("reservationTime").toLocalTime();

            this.documentsMap.put(numeroDVD, new DVD(numeroDVD, titre, adulte, abonnesMap.get(empruntePar), abonnesMap.get(reservePar), reservationTime));
        }

        // TODO: remove this for the final version
        System.out.println("=========================================");
        System.out.println("Table Abonnes :");
        this.abonnesMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
        System.out.println("=========================================");
        System.out.println("Table DVD :");
        this.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
        System.out.println("=========================================");
    }

    /**
     * Connect to the test.db database
     * @return the Connection object
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.pathDatabase);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Update the database with the new data document
     * @param numero the document number
     * @param empruntePar the subscriber who borrowed the document
     * @param reservePar the subscriber who reserved the document
     * @param reservationTime the time when the document was reserved
     */
    public void updateDVD(int numero, Subscriber empruntePar, Subscriber reservePar, LocalTime reservationTime) {
        String sql = "UPDATE DVD SET empruntePar = ? , "
                + "reservePar = ? , "
                + "reservationTime = ? "
                + "WHERE numero = ?";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // connect to the database
            conn = this.connect();
            if(conn == null)
                return;

            // set auto-commit mode to false
            conn.setAutoCommit(false);


            pstmt = conn.prepareStatement(sql);
            if (empruntePar == null)
                pstmt.setNull(1, Types.INTEGER);
            else
                pstmt.setInt(1, empruntePar.getNumero());
            if (reservePar == null)
                pstmt.setNull(2, Types.INTEGER);
            else
                pstmt.setInt(2, reservePar.getNumero());
            pstmt.setTime(3, Time.valueOf(reservationTime));
            pstmt.setInt(4, numero);

            //
            pstmt.executeUpdate();
            // commit work
            conn.commit();

        } catch (SQLException e1) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
            System.out.println(e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e3) {
                System.out.println(e3.getMessage());
            }
        }
    }

    /**
     * Reserve a document
     * @param numeroDocument the document number
     * @param numeroAbonne the subscriber number
     * @throws SQLException if the database is not found
     */
    public void reservation(int numeroDocument, int numeroAbonne) throws SQLException, DocumentNotAvailableException, SubscriberTooYoungException {
        DVD document = (DVD) this.documentsMap.get(numeroDocument);
        Subscriber subscriber = this.abonnesMap.get(numeroAbonne);
        if(document == null)
            return;
        if (document.reservePar() != null && document.reservePar().getNumero() != numeroAbonne)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), document.reservePar() != null);
        if (document.empruntePar() != null)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), false);
        if (document.getClass() == DVD.class && document.isForAdults() && isTooYoung(subscriber))
            throw new SubscriberTooYoungException();
        document.reservation(subscriber);
        updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());

        System.out.println("Reservation du document " + numeroDocument + " par l'abonne " + numeroAbonne);
        this.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }

    /**
     * Emprunt a document
     * @param numeroDocument the document number
     * @param numeroAbonne the subscriber number
     * @throws SQLException if the database is not found
     */
    public void emprunt(int numeroDocument, int numeroAbonne) throws SQLException, DocumentNotAvailableException, SubscriberTooYoungException {
        DVD document = (DVD) this.documentsMap.get(numeroDocument);
        Subscriber subscriber = this.abonnesMap.get(numeroAbonne);
        if (document == null)
            return;
        if (document.reservePar() != null && document.reservePar().getNumero() != numeroAbonne)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), document.reservePar() != null);
        if (document.empruntePar() != null)
            throw new DocumentNotAvailableException(numeroDocument, document.getRemainingReservationTime(), false);
        if (document.getClass() == DVD.class && document.isForAdults() && isTooYoung(subscriber))
            throw new SubscriberTooYoungException();
        document.emprunt(subscriber);
        updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());

        System.out.println("Emprunt du document " + numeroDocument + " par l'abonne " + numeroAbonne);
        this.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }

    /**
     * Return a document
     * @param numeroDocument the document number
     * @throws SQLException if the database is not found
     */
    public void retour(int numeroDocument) throws SQLException {
        DVD document = (DVD) this.documentsMap.get(numeroDocument);
        if (document == null)
            return;
        document.retour();
        updateDVD(numeroDocument, document.empruntePar(), document.reservePar(), document.getReservationTime());

        System.out.println("Retour du document " + numeroDocument);
        this.documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }

    /**
     * Return a catalog of documents
     * @return catalog of documents
     */
    public String getCatalogue() {
        StringBuilder catalog = new StringBuilder("=============== Catalogue ===============\n");
        for (Map.Entry<Integer, Document> entry : this.documentsMap.entrySet()) {
            catalog.append(entry.getValue().toString()).append("\n");
        }
        catalog.append("=========================================");
        return catalog.toString();
    }

    /**
     * Verify if a document exist
     * @param numeroDocument the document number
     * @return true if existed, false otherwise
     */
    public boolean documentNotExist(int numeroDocument) {
        return !this.documentsMap.containsKey(numeroDocument);
    }

    /**
     * Verify if a abonne exist
     * @param numeroAbonne abonne number
     * @return true if existed, false otherwise
     */
    public boolean abonneNotExist(int numeroAbonne) {
        return !this.abonnesMap.containsKey(numeroAbonne);
    }

    public int getNbLignesCatalogue() {
        return this.documentsMap.size() + 2; // +2 for the header and footer
    }

    /**
     * Return true if the subscriber is an adult
     * @param ab the subscriber
     * @return true if the subscriber is an adult
     */
    private boolean isTooYoung(Subscriber ab) {
        return ab.getAge() < AGE_MINIMUM;
    }
}
