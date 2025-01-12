package dao;
import model.Participant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDAO {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/funfitdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Passsword";

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Retrieve all participants
    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        String query = "SELECT * FROM Participants";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Participant participant = new Participant();
                participant.setParticipantId(rs.getInt("participantId"));
                participant.setName(rs.getString("name"));
                participant.setEmail(rs.getString("email"));
                participant.setBatchId(rs.getInt("batchId"));
                participants.add(participant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    // Add a new participant
    public boolean addParticipant(Participant participant) {
        String query = "INSERT INTO Participants (name, email, batchId) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, participant.getName());
            stmt.setString(2, participant.getEmail());
            stmt.setInt(3, participant.getBatchId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing participant
    public boolean updateParticipant(Participant participant) {
        String query = "UPDATE Participants SET name = ?, email = ?, batchId = ? WHERE participantId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, participant.getName());
            stmt.setString(2, participant.getEmail());
            stmt.setInt(3, participant.getBatchId());
            stmt.setInt(4, participant.getParticipantId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a participant
    public boolean deleteParticipant(int participantId) {
        String query = "DELETE FROM Participants WHERE participantId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, participantId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve a participant by ID
    public Participant getParticipantById(int participantId) {
        String query = "SELECT * FROM Participants WHERE participantId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, participantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Participant participant = new Participant();
                    participant.setParticipantId(rs.getInt("participantId"));
                    participant.setName(rs.getString("name"));
                    participant.setEmail(rs.getString("email"));
                    participant.setBatchId(rs.getInt("batchId"));
                    return participant;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve participants by batch ID
    public List<Participant> getParticipantsByBatchId(int batchId) {
        List<Participant> participants = new ArrayList<>();
        String query = "SELECT * FROM Participants WHERE batchId = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, batchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Participant participant = new Participant();
                    participant.setParticipantId(rs.getInt("participantId"));
                    participant.setName(rs.getString("name"));
                    participant.setEmail(rs.getString("email"));
                    participant.setBatchId(rs.getInt("batchId"));
                    participants.add(participant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }
    public boolean isBatchIdValid(int batchId) {
        String query = "SELECT COUNT(*) FROM Batches WHERE batchId = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the driver is loaded
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, batchId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if batchId exists
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

}
