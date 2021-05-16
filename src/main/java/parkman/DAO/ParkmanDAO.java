package parkman.DAO;

import javafx.scene.image.Image;
import parkman.Models.Transaction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;

public class ParkmanDAO {
    private static ParkmanDAO instance;
    private Connection conn;

    private PreparedStatement selectTransactionQuery, createInitialTransactionQuery, updatePlateByIdQuery, updateExitTimestampQuery;

    public static ParkmanDAO getInstance() {
        if (instance == null) instance = new ParkmanDAO();
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }

    private ParkmanDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:db/parkmanDb.sqlite");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Query
            selectTransactionQuery = conn.prepareStatement("SELECT * FROM main.\"transaction\" ORDER BY entranceTimestamp DESC;");
            createInitialTransactionQuery = conn.prepareStatement("INSERT INTO \"transaction\" (carPhoto, entranceTimestamp, pricePerHour, parkingSpot) values (?, ?,  ?, ?);");
            updatePlateByIdQuery = conn.prepareStatement("UPDATE main.\"transaction\" SET plateNumber = ?, platePhoto = ? WHERE id = ?");
            updateExitTimestampQuery = conn.prepareStatement("UPDATE main.\"transaction\" SET exitTimestamp = ? WHERE plateNumber = ? AND exitTImestamp IS NULL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeInstance() {
        if (instance == null) return;
        instance.close();
        instance = null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            ResultSet rs = selectTransactionQuery.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt(1),
                        rs.getBytes(2),
                        rs.getBytes(3),
                        rs.getString(4),
                        rs.getTimestamp(5),
                        rs.getTimestamp(6),
                        rs.getFloat(7),
                        rs.getString(8)
                    )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public int addInitialTransaction(Transaction transaction) {
        try {
            createInitialTransactionQuery.setBytes(1, transaction.getCarPicture());
            createInitialTransactionQuery.setTimestamp(2, transaction.getEntranceTimestamp());
            createInitialTransactionQuery.setFloat(3, transaction.getPricePerHour());
            createInitialTransactionQuery.setString(4, transaction.getParkingSpot());
            createInitialTransactionQuery.execute();

            ResultSet rs = createInitialTransactionQuery.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();

            return -1;
        }
    }

    public boolean updatePlateById(int insertId, String plateNumber) {
        try {
            updatePlateByIdQuery.setString(1, plateNumber);
            updatePlateByIdQuery.setBytes(2, Files.readAllBytes(new File("AI/src/output/output_final.png").toPath()));
            updatePlateByIdQuery.setInt(3, insertId);

            updatePlateByIdQuery.execute();

            return true;
        } catch(SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExitTimestampQuery(String plateNumber, Timestamp exitTimestamp) {
        try {
            updateExitTimestampQuery.setTimestamp(1, exitTimestamp);
            updateExitTimestampQuery.setString(2, plateNumber);

            updateExitTimestampQuery.execute();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
