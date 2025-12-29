package smartattendance.dao;

import smartattendance.model.Break;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BreakDAO {

    private final Connection conn;

    public BreakDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    /**
     * Find today's attendance_id for the user.
     * Returns -1 if not found.
     */
    public int findAttendanceIdForToday(int userId) {
        String sql = "SELECT attendance_id FROM attendance WHERE user_id = ? AND attendance_date = CURRENT_DATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("attendance_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Insert a new break row. Returns generated break_id or -1 on failure.
     * Assumes breaks table has columns: break_id, user_id, attendance_id, start_time, end_time, duration_minutes, created_at
     */
    public int startBreak(int userId, int attendanceId) {
        String sql = "INSERT INTO breaks (user_id, attendance_id, start_time, created_at) VALUES (?, ?, ?, now()) RETURNING break_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            ps.setInt(1, userId);
            ps.setInt(2, attendanceId);
            ps.setTimestamp(3, Timestamp.valueOf(now));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("break_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * End a break by id: set end_time and duration_minutes.
     */
    public boolean endBreak(int breakId) {
        String sql = "UPDATE breaks SET end_time = ?, duration_minutes = EXTRACT(EPOCH FROM (? - start_time))/60 WHERE break_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setInt(3, breakId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Find an open break for the given user & attendance (end_time IS NULL).
     * Returns break_id or -1 if none.
     */
    public int findOpenBreak(int userId, int attendanceId) {
        String sql = "SELECT break_id FROM breaks WHERE user_id = ? AND attendance_id = ? AND end_time IS NULL ORDER BY start_time DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, attendanceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("break_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Optional: get all breaks for an attendance (keeps your original method available)
     */
    public List<Break> getBreaksByAttendance(int attendanceId) {
        List<Break> list = new ArrayList<>();
        String sql = "SELECT * FROM breaks WHERE attendance_id = ? ORDER BY start_time";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attendanceId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Break b = new Break(
                        rs.getInt("break_id"),
                        rs.getInt("user_id"),
                        rs.getInt("attendance_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null,
                        rs.getLong("duration_minutes")
                );
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
