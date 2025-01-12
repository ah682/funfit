package dao;

import model.Batch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/funfitdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Passsword";

    private Connection connect() throws SQLException {
        try {
            // Load and register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Batch> getAllBatches() {
        List<Batch> batches = new ArrayList<>();
        String query = "SELECT * FROM batches";

        try (Connection conn = connect(); // Use the connect() method here
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Batch batch = new Batch();
                batch.setBatchId(rs.getInt("batchId"));
                batch.setBatchName(rs.getString("batchName"));
                batch.setTiming(rs.getString("timing"));
                batches.add(batch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return batches;
    }

    public boolean addBatch(Batch batch) {
        String query = "INSERT INTO Batches (batchName, timing) VALUES (?, ?)";

        try (Connection conn = connect(); // Use the connect() method here
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, batch.getBatchName());
            pstmt.setString(2, batch.getTiming());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean updateBatch(Batch batch) {
        String query = "UPDATE Batches SET batchName = ?, timing = ? WHERE batchId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, batch.getBatchName());
            pstmt.setString(2, batch.getTiming());
            pstmt.setInt(3, batch.getBatchId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
