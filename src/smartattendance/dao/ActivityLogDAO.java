package smartattendance.dao;

import smartattendance.model.ActivityLog;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAO {

    public void insertLog(int userId, String action, String description, String ipAddress) {
        String sql = "INSERT INTO activity_log (user_id, action, description, ip_address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, description);
            stmt.setString(4, ipAddress);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ActivityLog> getAllLogs() {
        List<ActivityLog> list = new ArrayList<>();

        String sql = "SELECT al.log_id, al.timestamp, u.full_name, al.action, al.description, al.ip_address " +
                     "FROM activity_log al JOIN users u ON al.user_id = u.user_id " +
                     "ORDER BY al.timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new ActivityLog(
                        rs.getInt("log_id"),
                        rs.getString("full_name"),
                        rs.getString("action"),
                        rs.getString("description"),
                        rs.getTimestamp("timestamp").toString(),
                        rs.getString("ip_address")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ActivityLog> searchLogs(String keyword) {
        List<ActivityLog> list = new ArrayList<>();

        String sql = "SELECT al.log_id, al.timestamp, u.full_name, al.action, al.description, al.ip_address " +
                     "FROM activity_log al JOIN users u ON al.user_id = u.user_id " +
                     "WHERE u.full_name ILIKE ? OR al.action ILIKE ? OR al.description ILIKE ? " +
                     "ORDER BY al.timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ActivityLog(
                            rs.getInt("log_id"),
                            rs.getString("full_name"),
                            rs.getString("action"),
                            rs.getString("description"),
                            rs.getTimestamp("timestamp").toString(),
                            rs.getString("ip_address")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteAllLogs() {
        String sql = "DELETE FROM activity_log";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
