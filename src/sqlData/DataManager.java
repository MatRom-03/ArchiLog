package sqlData;

import documents.DVD;
import documents.DocumentModel;
import subscribers.Abonne;

import java.nio.file.FileSystems;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static final String pathDatabase = "jdbc:sqlite:"+ FileSystems.getDefault().getPath("src/sqlData/Data.db").normalize().toAbsolutePath().toString();
    public static Map<Integer, Abonne> abonnesMap = new HashMap<>();
    public static Map<Integer, DocumentModel> documentsMap = new HashMap<>();

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
            LocalTime reservationTime = resultSetDVD.getTime("reservationTime").toLocalTime();

            documentsMap.put(numeroDVD, new DVD(numeroDVD, titre, abonnesMap.get(empruntePar), abonnesMap.get(reservePar), reservationTime, adulte));
        }

        // TODO: remove this for the final version
        System.out.println("=========================================");
        System.out.println("Table Abonnes :");
        abonnesMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
        System.out.println("=========================================");
        System.out.println("Table DVD :");
        documentsMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
        System.out.println("=========================================");
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
     * @param reservationTime the time when the document was reserved
     */
    public static void updateDVD(int numero, Abonne empruntePar, Abonne reservePar, LocalTime reservationTime) {
        // TODO : update the database with the new data forma : DocumentModel
        String sql = "UPDATE DVD SET empruntePar = ? , "
                + "reservePar = ? , "
                + "reservationTime = ? "
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
}
