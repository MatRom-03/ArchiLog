package sqlAndData;

import documents.DVD;
import documents.AbstractDocument;
import documents.DocumentStates;
import server.log.Log;
import subscribers.Abonne;

import java.nio.file.FileSystems;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static final String pathDatabase = "jdbc:sqlite:"+ FileSystems.getDefault().getPath("src/sqlAndData/Data.db").normalize().toAbsolutePath().toString();
    public static Map<Integer, Abonne> abonnesMap = new HashMap<>();
    public static Map<Integer, AbstractDocument> documentsMap = new HashMap<>();

    /**
     * Retreve all the data from the database
     * @throws SQLException if the database is not found
     */
    public static void getDataFromFile() throws SQLException {
        Connection connection = DriverManager.getConnection(pathDatabase);

        Statement statement = connection.createStatement();

        String queryAbonnes = "SELECT * FROM Abonnes";
        ResultSet resultSetAbonnes = statement.executeQuery(queryAbonnes);

        while (resultSetAbonnes.next()) {
            int numeroAbonne = resultSetAbonnes.getInt("numero");
            String name = resultSetAbonnes.getString("name");
            Date dateOfBirth = resultSetAbonnes.getDate("dateOfBirth");
            abonnesMap.put(numeroAbonne, new Abonne(numeroAbonne, name, dateOfBirth));
        }


        String queryDVD = "SELECT * FROM DVD";
        ResultSet resultSetDVD = statement.executeQuery(queryDVD);

        while (resultSetDVD.next()) {
            int numeroDVD = resultSetDVD.getInt("numero");
            String titre = resultSetDVD.getString("titre");
            boolean adulte = resultSetDVD.getBoolean("adulte");
            int empruntePar = resultSetDVD.getInt("empruntePar");
            int reservePar = resultSetDVD.getInt("reservePar");
            Timestamp reservationTimeStamp = resultSetDVD.getTimestamp("reservationTimeStamp");
            DocumentStates state = DocumentStates.fromString(resultSetDVD.getString("state"));

            documentsMap.put(numeroDVD, new DVD(numeroDVD, titre, abonnesMap.get(empruntePar), abonnesMap.get(reservePar), reservationTimeStamp, state,adulte));
        }
    }

    /**
     * Connect to the test.db database
     * @return the Connection object
     */
    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(pathDatabase);
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
     * @param reservationTimeStamp the Timestamp when the document was reserved
     */
    public static void updateDVD(int numero, Abonne empruntePar, Abonne reservePar, Timestamp reservationTimeStamp, DocumentStates state) {
        String sql = "UPDATE DVD SET empruntePar = ? , "
                + "reservePar = ? , "
                + "reservationTimeStamp = ? ,"
                + "state = ? "
                + "WHERE numero = ?";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // connect to the database
            conn = connect();
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
            pstmt.setTimestamp(3, reservationTimeStamp);
            pstmt.setString(4, state.toString());
            pstmt.setInt(5, numero);

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
     * Insert a new log in the database
     * @param log the log to insert
     */
    public static void insertLog(Log log){
        String sql = "INSERT INTO Logs(timeStamp, action, numeroAbonne, numeroDocument) VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, log.getTimeStamp());
            pstmt.setString(2, log.getAction());
            pstmt.setInt(3, log.getNumeroAbonne());
            pstmt.setInt(4, log.getNumeroDocument());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}