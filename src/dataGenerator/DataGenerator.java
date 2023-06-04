package dataGenerator;

import java.nio.file.FileSystems;
import java.sql.*;

public class DataGenerator {
    private static final String url = "jdbc:sqlite:" + FileSystems.getDefault().getPath("src/dataGenerator/Data.db").normalize().toAbsolutePath().toString();

    /**
     * Drop the tables
     */
    public static void dropTables() {
        String sql = "DROP TABLE IF EXISTS Abonnes";
        String sql2 = "DROP TABLE IF EXISTS DVD";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a new database
     */
    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create the tables
     */
    public static void createTables() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Abonnes (\n" +
                "  numero INT PRIMARY KEY,\n" +
                "  name VARCHAR(255) NOT NULL,\n" +
                "  dateOfBirth DATE NOT NULL\n" +
                ");\n";

        String sql2 = "CREATE TABLE IF NOT EXISTS DVD (\n" +
                "  numero INT PRIMARY KEY,\n" +
                "  titre VARCHAR(255) NOT NULL,\n" +
                "  adulte BOOLEAN NOT NULL,\n" +
                "  empruntePar INT,\n" +
                "  reservePar INT,\n" +
                "  reservationTimeStamp TIMESTAMP,\n" +
                "  state VARCHAR(255) NOT NULL,\n" +
                "  FOREIGN KEY (empruntePar) REFERENCES Abonnes(numero),\n" +
                "  FOREIGN KEY (reservePar) REFERENCES Abonnes(numero)\n" +
                ");\n";

        String sql3 = "CREATE TABLE IF NOT EXISTS Logs (\n" +
                "  timeStamp TIMESTAMP PRIMARY KEY,\n" +
                "  action VARCHAR(255) NOT NULL,\n" +
                "  numeroAbonne INT,\n" +
                "  numeroDocument INT\n" +
                ");\n";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert a new row into the abonnes table
     *
     * @param name the name of the person
     * @param numero the numero of the person
     * @param dateOfBirth the date of birth of the person
     */
    public void insertAbonnes(int numero, String name, Date dateOfBirth) {
        String sql = "INSERT INTO Abonnes (numero, name, dateOfBirth) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
            pstmt.setString(2, name);
            pstmt.setDate(3, dateOfBirth);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the DVD table
     * @param numero numero of the DVD
     * @param titre title of the DVD
     * @param adulte is the DVD for adults only
     * @param empruntePar numero of the person who borrowed the DVD
     * @param reservePar numero of the person who reserved the DVD
     */
    public void insertDVD(int numero, String titre, boolean adulte, Integer empruntePar, Integer reservePar, Timestamp reservationTimeStamp, String state) {
        String sql = "INSERT INTO DVD (numero, titre, adulte, empruntePar, reservePar, reservationTimeStamp, state) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
            pstmt.setString(2, titre);
            pstmt.setBoolean(3, adulte);
            pstmt.setNull(4, Types.INTEGER);
            pstmt.setNull(5, Types.INTEGER);
            pstmt.setTimestamp(6, reservationTimeStamp);
            pstmt.setString(7, state);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {

        Timestamp reservationTimeStamp = new Timestamp(System.currentTimeMillis());

        createNewDatabase();

        dropTables();
        createTables();

        DataGenerator app = new DataGenerator();
        app.insertAbonnes(1, "Matteo", Date.valueOf("2003-06-30"));
        app.insertAbonnes(2, "toto", Date.valueOf("2009-08-30"));
        app.insertAbonnes(3, "Arthur", Date.valueOf("2003-06-10"));

        app.insertDVD(1, "Une vie de bestiole", false, null, null, reservationTimeStamp, "Disponible");
        app.insertDVD(2, "Mille et une façons de mourir dans l'ouest", true, null, null, reservationTimeStamp, "Disponible");
        app.insertDVD(3, "Batman : Le commencement", false, null, null, reservationTimeStamp, "Disponible");
        app.insertDVD(4, "Poulets en fuite", false, null, null, reservationTimeStamp, "Disponible");
        app.insertDVD(5, "Le monde de Nemo", false, null, null, reservationTimeStamp, "Disponible");
        app.insertDVD(6, "Le monde de Dory", false, null, null, reservationTimeStamp, "Disponible");

        String url = "jdbc:sqlite:C:\\Users\\matte\\IdeaProjects\\test\\src\\Data.db";
        Connection connection = DriverManager.getConnection(url);

        Statement statement = connection.createStatement();

        System.out.println("====================================");

        String queryAbonnes = "SELECT * FROM Abonnes";
        ResultSet resultSetAbonnes = statement.executeQuery(queryAbonnes);

        while (resultSetAbonnes.next()) {
            System.out.println("Abonnes :" + " " + resultSetAbonnes.getInt("numero") + " " + resultSetAbonnes.getString("name") + " " + resultSetAbonnes.getDate("dateOfBirth"));
        }

        System.out.println("====================================");

        String queryDVD = "SELECT * FROM DVD";
        ResultSet resultSetDVD = statement.executeQuery(queryDVD);

        while (resultSetDVD.next()) {
            System.out.println("DVD :" + " " + resultSetDVD.getInt("numero") + " " + resultSetDVD.getString("titre") + " " + resultSetDVD.getBoolean("adulte") + " " + resultSetDVD.getInt("empruntePar") + " " + resultSetDVD.getInt("reservePar") + " " + resultSetDVD.getTimestamp("reservationTimeStamp") + " " + resultSetDVD.getString("state"));
        }

        System.out.println("====================================");

        resultSetAbonnes.close();
        resultSetDVD.close();
        statement.close();
        connection.close();



    }
}