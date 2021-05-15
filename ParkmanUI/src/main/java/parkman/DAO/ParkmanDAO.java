package parkman.DAO;

import parkman.Models.Transaction;

import java.sql.*;
import java.util.ArrayList;

public class ParkmanDAO {
    private static ParkmanDAO instance;
    private Connection conn;

    private PreparedStatement selectTransactionQuery, createInitialTransactionQuery, updatePlateByIdQuery;

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
            selectTransactionQuery = conn.prepareStatement("SELECT * FROM main.\"transaction\";");
        } catch (SQLException e) {
            try {
                selectTransactionQuery = conn.prepareStatement("SELECT * FROM main.\"transaction\";");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        try {
            // Query
            selectTransactionQuery = conn.prepareStatement("SELECT * FROM main.\"transaction\";");
            createInitialTransactionQuery = conn.prepareStatement("INSERT INTO \"transaction\" (carPhoto, entranceTimestamp, pricePerHour, parkingSpot) values (?, ?,  ?, ?);");
            updatePlateByIdQuery = conn.prepareStatement("UPDATE main.\"transaction\" SET plateNumber = ? WHERE id = ?");
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
            updatePlateByIdQuery.setInt(2, insertId);

            updatePlateByIdQuery.execute();

            return true;
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
}
